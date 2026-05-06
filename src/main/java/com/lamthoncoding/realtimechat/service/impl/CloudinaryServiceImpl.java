package com.lamthoncoding.realtimechat.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.lamthoncoding.realtimechat.entity.User;
import com.lamthoncoding.realtimechat.exception.handlers.EntityNotFound;
import com.lamthoncoding.realtimechat.exception.handlers.InvalidInputException;
import com.lamthoncoding.realtimechat.repository.UserRepository;
import com.lamthoncoding.realtimechat.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;
    private final UserRepository userRepository;
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    private static final List<String> IMAGE_TYPES = List.of(
            "image/jpeg", "image/png", "image/jpg",
            "image/heic", "image/heif", "image/webp", "image/gif"
    );

    private static final List<String> DOCUMENT_TYPES = List.of(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    );

    @Override
    public String uploadAvatar(MultipartFile file) {

        validateFile(file, IMAGE_TYPES);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFound("User not found"));

        try {
            String url = uploadToCloudinary(file, "avatars", "image");

            user.setAvatar(url);
            userRepository.save(user);

            return url;

        } catch (IOException e) {
            log.error("Upload avatar failed", e);
            throw new RuntimeException("Upload avatar failed");
        }
    }

    @Override
    public List<String> uploadImage(List<MultipartFile> files) {

        return files.stream()
                .filter(file -> !file.isEmpty())
                .map(file -> {
                    validateFile(file, IMAGE_TYPES);
                    try {
                        return uploadToCloudinary(file, "chat-images", "image");
                    } catch (IOException e) {
                        throw new IllegalArgumentException("Upload image failed", e);
                    }
                })
                .toList();
    }

    @Override
    public List<String> uploadFile(List<MultipartFile> files) {

        return files.stream()
                .filter(file -> !file.isEmpty())
                .map(file -> {
                    validateFile(file, DOCUMENT_TYPES);
                    try {
                        return uploadToCloudinary(file, "chat-files", "raw");
                    } catch (IOException e) {
                        throw new IllegalArgumentException("Upload file failed", e);
                    }
                })
                .toList();
    }

    private void validateFile(MultipartFile file, List<String> allowedTypes) {

        if (file == null || file.isEmpty()) {
            throw new InvalidInputException("File is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new InvalidInputException("File exceeds max size 5MB");
        }

        String contentType = file.getContentType();

        if (contentType == null || !allowedTypes.contains(contentType)) {
            throw new InvalidInputException("Unsupported file type: " + contentType);
        }
    }

    private String uploadToCloudinary(MultipartFile file, String folder, String resourceType) throws IOException {

        String fileName = getFileNameWithExtension(file);

        Map<?, ?> result = cloudinary.uploader().upload(
                file.getBytes(),
                Map.of(
                        "folder", folder,
                        "resource_type", resourceType,
                        "type", "upload",
                        "public_id", fileName,
                        "unique_filename", true
                )
        );

        return result.get("secure_url").toString();
    }

    private String getFileNameWithExtension(MultipartFile file) {
        String original = file.getOriginalFilename();

        if (original == null || !original.contains(".")) {
            throw new InvalidInputException("File must have extension");
        }

        return original.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
    }
}
