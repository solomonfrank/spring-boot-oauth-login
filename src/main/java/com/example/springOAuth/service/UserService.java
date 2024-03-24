package com.example.springOAuth.service;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.springOAuth.entity.User;
import com.example.springOAuth.model.UserDto;
import com.example.springOAuth.model.UserProfileRequest;
import com.example.springOAuth.repository.UserRepository;
import com.example.springOAuth.service.upload.CloudinaryStrategy;
import com.example.springOAuth.service.upload.UploadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    @Autowired
    private UploadService uploadService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CloudinaryStrategy cloudinaryStrategy;

    public UserDto updateProfile(User currentUser, UserProfileRequest entity) throws IOException {

        var user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        uploadService.setStrategy(cloudinaryStrategy);

        if (entity.getImageFile() != null) {
            var url = uploadService.uploadFile(entity.getImageFile());
            user.setImageUrl(url);
        }

        user.setName(entity.getName());
        var savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);

    }

}
