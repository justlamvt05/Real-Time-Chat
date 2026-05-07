package com.lamthoncoding.realtimechat.service;

import com.lamthoncoding.realtimechat.payload.response.ApiResponse;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public interface ChatRoomUserService {

     ApiResponse<?> getOrCreatePrivateRoom( UUID userB);

    @Transactional
    ApiResponse<?> createGroup(
            String groupName,
            List<UUID> userIds
    );

    @Transactional
    ApiResponse<?> addUsersToGroup(
            UUID roomId,
            List<UUID> userIds
    );

    ApiResponse<?> getMyChats( int page, int size);
}
