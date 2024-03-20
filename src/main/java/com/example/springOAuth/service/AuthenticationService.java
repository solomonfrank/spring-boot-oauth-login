package com.example.springOAuth.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.springOAuth.entity.IdentityProvider;
import com.example.springOAuth.entity.User;
import com.example.springOAuth.exception.DuplicateUserInfoException;
import com.example.springOAuth.exception.IdentityLoginErrorException;
import com.example.springOAuth.model.LoginRequest;
import com.example.springOAuth.model.RegisterRequest;
import com.example.springOAuth.model.UserDto;
import com.example.springOAuth.repository.UserRepository;
import com.example.springOAuth.response.AuthenticationResponse;
import com.example.springOAuth.security.JwtService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final ModelMapper modelMapper;

    @Transactional
    public AuthenticationResponse authenticate(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));

        if (user.getIdentityProvider().name() == IdentityProvider.GOOGLE.name()
                || user.getIdentityProvider().name() == IdentityProvider.GITHUB.name()) {
            throw new IdentityLoginErrorException("Your account was created using an Identity Provider.");
        }

        var authenticateAction = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticateAction);

        Map<String, Object> claims = new HashMap<>();

        var u = user.getAuthorities();

        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        claims.put("name", user.getName());
        claims.put("preferred_username", user.getUsername());
        claims.put("roles", roles);

        String jwt = jwtService.generateToken(claims, user);
        var userMapper = modelMapper.map(user, UserDto.class);

        return AuthenticationResponse.builder().token(jwt).user(userMapper).build();

    }

    public AuthenticationResponse registerHandler(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {

            throw new DuplicateUserInfoException("Username or email is already taken");

        }

        var user = User.builder().email(request.getEmail()).name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .emailVerified(false).identityProvider(IdentityProvider.LOCAL).build();

        UserDetails savedUser = userRepository.save(user);
        var token = jwtService.generateToken(savedUser);
        var userMapper = modelMapper.map(savedUser, UserDto.class);

        return AuthenticationResponse.builder().token(token).user(userMapper).build();
    }
}
