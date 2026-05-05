package com.lamthoncoding.realtimechat.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CloudinaryService {
    String uploadAvatar(MultipartFile file);
    List<String> uploadImage(List<MultipartFile> file);

}
