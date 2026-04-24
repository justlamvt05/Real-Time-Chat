package com.lamthoncoding.realtimechat.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.lamthoncoding.realtimechat.entity.User;
import com.lamthoncoding.realtimechat.exception.handlers.EntityNotFound;
import com.lamthoncoding.realtimechat.repository.UserRepository;
import com.lamthoncoding.realtimechat.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.Map;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;
    private final UserRepository userRepository;
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    private static final List<String> ALLOWED_IMAGE_TYPES = List.of(
            "image/jpeg",
            "image/png",
            "image/jpg",
            "image/heic",
            "image/heif",
            "image/webp",
            "image/gif"
    );

    @Override
    public String uploadAvatar(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        // Check dung lượng
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("File size exceeds 5MB");
        }

        // Check định dạng ảnh
        String contentType = file.getContentType();

        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType)) {
            throw new RuntimeException("Unsupported image format");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;

        String username = authentication.getName();
        log.info("Username: {}", username);

        User currentUser = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new EntityNotFound("User Not Found"));

        try {

            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "avatars"
                    )
            );

            String imageUrl = uploadResult.get("secure_url").toString();

            currentUser.setAvatar(imageUrl);
            userRepository.save(currentUser);

            return imageUrl;

        } catch (IOException e) {
            throw new RuntimeException("Upload failed");
        }
    }
}
