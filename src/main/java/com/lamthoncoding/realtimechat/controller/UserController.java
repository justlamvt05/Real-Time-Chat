package com.lamthoncoding.realtimechat.controller;

import com.lamthoncoding.realtimechat.dto.UserDto;
import com.lamthoncoding.realtimechat.payload.request.EditProfileRequest;
import com.lamthoncoding.realtimechat.payload.response.ApiResponse;
import com.lamthoncoding.realtimechat.service.CloudinaryService;
import com.lamthoncoding.realtimechat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final CloudinaryService cloudinaryService;
    private final UserService userService;

    @PostMapping("/avatar")
    public ResponseEntity<?> uploadAvatar(
            @RequestParam("file") MultipartFile file
    ) {

        String imageUrl = cloudinaryService.uploadAvatar(file);

        return ResponseEntity.ok(imageUrl);
    }

    @PutMapping("/profile")
    public ApiResponse<?> editProfile(
            @RequestBody EditProfileRequest request,
            Authentication authentication
    ) {

        String username = authentication.getName();

        return ApiResponse.success(userService.editProfile(request, username));
    }

}