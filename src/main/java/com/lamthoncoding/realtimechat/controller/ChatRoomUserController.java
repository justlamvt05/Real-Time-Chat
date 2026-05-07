package com.lamthoncoding.realtimechat.controller;

import com.lamthoncoding.realtimechat.payload.request.CreateGroupRequest;
import com.lamthoncoding.realtimechat.payload.request.AddUsersToGroupRequest;
import com.lamthoncoding.realtimechat.payload.response.ApiResponse;
import com.lamthoncoding.realtimechat.repository.UserRepository;
import com.lamthoncoding.realtimechat.service.ChatRoomService;
import com.lamthoncoding.realtimechat.service.ChatRoomUserService;
import com.lamthoncoding.realtimechat.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
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
    public ApiResponse<?> createPrivateRoom(
            @RequestParam UUID userId
    ) {
        return chatRoomUserService.getOrCreatePrivateRoom(
                userId
        );
    }

    @PostMapping("/group")
    public ApiResponse<?> createGroup(
            @RequestBody CreateGroupRequest request
    ) {

        return chatRoomUserService.createGroup(
                request.getGroupName(),
                request.getUserIds()
        );
    }

    @PostMapping("/group/{roomId}/members")
    public ApiResponse<?> addUsersToGroup(
            @PathVariable UUID roomId,
            @RequestBody AddUsersToGroupRequest request
    ) {

        return chatRoomUserService.addUsersToGroup(
                roomId,
                request.getUserIds()
        );
    }

    @GetMapping("/my-chat")
    public ApiResponse<?> getMyChats(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        return chatRoomUserService.getMyChats(
                page,
                size
        );
    }

    @PostMapping("/upload/image")
    public ApiResponse<?> uploadImage(
            @RequestParam("file")
            List<MultipartFile> file
    ) {

        List<String> imageUrl =
                cloudinaryService.uploadImage(file);

        return ApiResponse.success(imageUrl);
    }

    @PostMapping("/upload/file")
    public ApiResponse<?> uploadFile(
            @RequestParam("file")
            List<MultipartFile> files
    ) {

        List<String> fileUrl =
                cloudinaryService.uploadFile(files);

        return ApiResponse.success(fileUrl);
    }
}