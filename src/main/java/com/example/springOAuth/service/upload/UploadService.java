package com.example.springOAuth.service.upload;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.Setter;

@Service
@Setter
public class UploadService {

    private IUploadStrategy strategy;

    public String uploadFile(MultipartFile file) throws IOException {

        return strategy.upload(file);

    }

}
