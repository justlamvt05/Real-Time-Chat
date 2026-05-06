package com.lamthoncoding.realtimechat.controller;

import com.lamthoncoding.realtimechat.payload.response.ApiResponse;
import com.lamthoncoding.realtimechat.payload.response.MessageResponse;
import com.lamthoncoding.realtimechat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/{chatRoomId}")
    public ApiResponse<Page<MessageResponse>> getMessages(
            @PathVariable UUID chatRoomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        return messageService.getMessages(chatRoomId, page, size);

    }

    @PostMapping("/delete")
    public ApiResponse<String> deleteMessages(@RequestParam UUID messageId ) {

        return messageService.deleteMessage( messageId);
    }

    @PutMapping("/edit")
    public ApiResponse<String> editMessages(@RequestParam UUID messageId, @RequestParam String content ) {
        return messageService.editMessage(messageId, content);
    }
}