package com.lamthoncoding.realtimechat.service;

import com.lamthoncoding.realtimechat.payload.request.ChatMessageRequest;

public interface ChatRoomService {
    void handleMessage(ChatMessageRequest request);
}
