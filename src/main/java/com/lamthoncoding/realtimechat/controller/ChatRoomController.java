package com.lamthoncoding.realtimechat.controller;

import com.lamthoncoding.realtimechat.payload.request.ChatMessageRequest;
import com.lamthoncoding.realtimechat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessageRequest request) {
        chatRoomService.handleMessage(request);
    }
}
