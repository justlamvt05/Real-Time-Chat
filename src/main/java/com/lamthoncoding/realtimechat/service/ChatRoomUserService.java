package com.lamthoncoding.realtimechat.service;

import com.lamthoncoding.realtimechat.payload.response.ApiResponse;

import java.util.UUID;

public interface ChatRoomUserService {

     ApiResponse<?> getOrCreatePrivateRoom(UUID userA, UUID userB);

    ApiResponse<?> getMyChats(UUID userId, int page, int size);
}
