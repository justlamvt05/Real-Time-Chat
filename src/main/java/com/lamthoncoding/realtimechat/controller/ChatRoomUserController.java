package com.lamthoncoding.realtimechat.controller;

import com.lamthoncoding.realtimechat.entity.User;
import com.lamthoncoding.realtimechat.exception.handlers.EntityNotFound;
import com.lamthoncoding.realtimechat.payload.response.ApiResponse;
import com.lamthoncoding.realtimechat.repository.UserRepository;
import com.lamthoncoding.realtimechat.service.ChatRoomService;
import com.lamthoncoding.realtimechat.service.ChatRoomUserService;
import com.lamthoncoding.realtimechat.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomUserController {
    private final ChatRoomUserService chatRoomUserService;
    private final ChatRoomService chatRoomService;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    @PostMapping("/private-room")
    public ApiResponse<?> createPrivateRoom(@RequestParam UUID userId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assert auth != null;
        String username = auth.getName();

        User currentUser = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new EntityNotFound("User Not Found"));

        return ApiResponse.success(chatRoomUserService.getOrCreatePrivateRoom(currentUser.getId(), userId));
    }


    @PostMapping("/upload/image")
    public ApiResponse<?> uploadImage(
            @RequestParam("file") List<MultipartFile> file
    ) {

        List<String> imageUrl = cloudinaryService.uploadImage(file);

        return ApiResponse.success(imageUrl);
    }

//    @GetMapping("/get/image")
//    public ResponseEntity<?> getImage(@RequestParam UUID roomId) {
//
//    }

    @PostMapping("/upload/file")
    public ApiResponse<?> uploadFile(
            @RequestParam("file") List<MultipartFile> files
    ) {

        List<String> fileUrl = cloudinaryService.uploadFile(files);

        return ApiResponse.success(fileUrl);
    }

    @GetMapping("/my-chat")
    public ApiResponse<?> getMyChats(
            @RequestParam UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        return chatRoomUserService.getMyChats(userId, page, size);
    }
}
