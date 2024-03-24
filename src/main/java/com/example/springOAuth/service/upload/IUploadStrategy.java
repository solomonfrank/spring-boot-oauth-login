package com.example.springOAuth.service.upload;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface IUploadStrategy {

    String upload(MultipartFile file) throws IOException;

}
