package com.example.springOAuth.service.upload;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Component
public class CloudinaryStrategy implements IUploadStrategy {

    public String cloudinaryUrl;

    private Cloudinary cloudinary;

    public CloudinaryStrategy(@Value("${app.cloudinary.url}") String cloudinaryUrl) {

        this.cloudinary = new Cloudinary(cloudinaryUrl);
        this.cloudinary.config.secure = true;
        this.cloudinaryUrl = cloudinaryUrl;
    }

    @Override
    public String upload(MultipartFile file) throws IOException {

        var name = file.getOriginalFilename().split("\\.");

        var filename = name[0];

        // Upload the image
        Map<?, ?> params1 = ObjectUtils.asMap("public_id", filename, "use_filename", "true", "folder",
                "springoauth");
        var response = this.cloudinary.uploader()
                .upload(file.getBytes(), params1);

        return response.get("secure_url").toString();

    }

}
