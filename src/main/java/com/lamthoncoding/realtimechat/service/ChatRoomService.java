package com.lamthoncoding.realtimechat.service;

import com.lamthoncoding.realtimechat.payload.request.ChatMessageRequest;

import java.security.Principal;

public interface ChatRoomService {
    void handleMessage(ChatMessageRequest request, Principal principal);
}
