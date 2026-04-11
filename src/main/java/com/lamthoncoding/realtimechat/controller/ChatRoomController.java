package com.lamthoncoding.realtimechat.controller;

import com.lamthoncoding.realtimechat.payload.request.ChatMessageRequest;
import com.lamthoncoding.realtimechat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessageRequest request, Principal principal) {
        chatRoomService.handleMessage(request, principal);
    }
}
