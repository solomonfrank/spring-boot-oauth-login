package com.example.springOAuth.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.springOAuth.entity.User;
import com.example.springOAuth.repository.CredentialRepository;
import com.example.springOAuth.repository.UserRepository;
import com.example.springOAuth.response.CredentialResponse;

@Service
public class CredentialService {

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<CredentialResponse> getInstalledAppHandler(User currentUser) {

        var user = userRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("user not exist"));

        var result = credentialRepository.findByUser(user);

        List<CredentialResponse> response = result.stream()
                .map(credential -> modelMapper.map(credential, CredentialResponse.class)).toList();

        return response;

    }

}
