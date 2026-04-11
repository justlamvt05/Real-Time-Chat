package com.lamthoncoding.realtimechat.service.impl;

import com.lamthoncoding.realtimechat.entity.ChatRoom;
import com.lamthoncoding.realtimechat.entity.ChatRoomUser;
import com.lamthoncoding.realtimechat.entity.User;
import com.lamthoncoding.realtimechat.payload.response.ApiResponse;
import com.lamthoncoding.realtimechat.repository.ChatRoomRepository;
import com.lamthoncoding.realtimechat.repository.ChatRoomUserRepository;
import com.lamthoncoding.realtimechat.repository.UserRepository;
import com.lamthoncoding.realtimechat.service.ChatRoomUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomUserServiceImpl implements ChatRoomUserService {
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public ApiResponse<?> getOrCreatePrivateRoom(UUID userA, UUID userB) {
        List<UUID> userIds = List.of(userA, userB);

        Optional<UUID> existingRoomId = chatRoomUserRepository.findPrivateRoom(userIds, 2);

        if (existingRoomId.isPresent()) {
            return ApiResponse.success(existingRoomId.get()) ;
        }

        ChatRoom room = new ChatRoom();
        room.setIsGroup(false);
        chatRoomRepository.save(room);

        User u1 = userRepository.findById(userA).orElseThrow();
        User u2 = userRepository.findById(userB).orElseThrow();

        chatRoomUserRepository.save(ChatRoomUser.builder()
                .chatRoom(room)
                .user(u1)
                .build());

        chatRoomUserRepository.save(ChatRoomUser.builder()
                .chatRoom(room)
                .user(u2)
                .build());

        return ApiResponse.success(room.getId()) ;
    }
}
