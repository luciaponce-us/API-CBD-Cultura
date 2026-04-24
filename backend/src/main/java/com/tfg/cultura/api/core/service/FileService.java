package com.tfg.cultura.api.core.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.tfg.cultura.api.core.exception.FileUploadException;
import com.tfg.cultura.api.core.model.dto.FileUploadRequest;

@Service
public class FileService {

    private Cloudinary cloudinary;

    public FileService (Cloudinary cloudinary){
        this.cloudinary=cloudinary;
    }

    public String uploadFile(FileUploadRequest request) {
        try {
            Map<String, Object> options = new HashMap<>();

            options.put("folder", request.getFolder());
            options.put("resource_type", request.getResourceType());

            if (request.getPublicId() != null) {
                options.put("public_id", request.getPublicId());
            }

            if (request.isOverwrite()) {
                options.put("overwrite", true);
            }

            if (request.getTransformation()!=null) {
                options.put("transformation",request.getTransformation());
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    request.getFile().getBytes(),
                    options
            );

            return uploadResult.get("secure_url").toString();

        } catch (Exception e) {
            throw new FileUploadException(e.getMessage());
        }
    }
    
}
