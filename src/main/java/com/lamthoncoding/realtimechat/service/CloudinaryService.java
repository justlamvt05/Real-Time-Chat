package com.lamthoncoding.realtimechat.service;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    String uploadAvatar(MultipartFile file);
}
