package com.lamthoncoding.realtimechat.service.impl;

import com.lamthoncoding.realtimechat.entity.ChatRoom;
import com.lamthoncoding.realtimechat.entity.ChatRoomUser;
import com.lamthoncoding.realtimechat.entity.User;
import com.lamthoncoding.realtimechat.exception.handlers.EntityNotFound;
import com.lamthoncoding.realtimechat.payload.response.ApiResponse;
import com.lamthoncoding.realtimechat.payload.response.ChatRoomResponse;
import com.lamthoncoding.realtimechat.repository.ChatRoomRepository;
import com.lamthoncoding.realtimechat.repository.ChatRoomUserRepository;
import com.lamthoncoding.realtimechat.repository.UserRepository;
import com.lamthoncoding.realtimechat.service.ChatRoomUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomUserServiceImpl implements ChatRoomUserService {

    private final ChatRoomUserRepository chatRoomUserRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public ApiResponse<?> getOrCreatePrivateRoom( UUID userB) {
        Authentication auth =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String username = auth.getName();

        User userA = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new EntityNotFound("User not found"));

        if (userA.getId().equals(userB)) {
            throw new IllegalArgumentException(
                    "Cannot create chat with yourself"
            );
        }

        List<UUID> userIds = List.of(userA.getId() , userB);

        Optional<UUID> existingRoomId =
                chatRoomUserRepository.findPrivateRoom(
                        userIds,
                        2
                );

        if (existingRoomId.isPresent()) {
            return ApiResponse.success(existingRoomId.get());
        }


        User u2 = userRepository.findById(userB)
                .orElseThrow(() ->
                        new EntityNotFound("User B not found"));

        ChatRoom room = new ChatRoom();
        room.setIsGroup(false);

        chatRoomRepository.save(room);

        List<ChatRoomUser> roomUsers = List.of(
                ChatRoomUser.builder()
                        .chatRoom(room)
                        .user(userA)
                        .joinedAt(Instant.now())
                        .build(),

                ChatRoomUser.builder()
                        .chatRoom(room)
                        .user(u2)
                        .joinedAt(Instant.now())
                        .build()
        );

        chatRoomUserRepository.saveAll(roomUsers);

        return ApiResponse.success(room.getId());
    }

    @Override
    public ApiResponse<?> createGroup(
            String groupName,
            List<UUID> userIds
    ) {

        if (groupName == null || groupName.isBlank()) {
            throw new IllegalArgumentException(
                    "Group name is required"
            );
        }

        if (userIds == null || userIds.isEmpty()) {
            throw new IllegalArgumentException(
                    "User list cannot be empty"
            );
        }

        Set<UUID> uniqueIds = new HashSet<>(userIds);

        if (uniqueIds.size() < 3) {
            throw new IllegalArgumentException(
                    "Group must have at least 3 people"
            );
        }

        List<User> users = userRepository.findAllById(uniqueIds);

        if (users.size() != uniqueIds.size()) {
            throw new EntityNotFound("User not found");
        }

        ChatRoom room = new ChatRoom();
        room.setName(groupName);
        room.setIsGroup(true);

        chatRoomRepository.save(room);

        List<ChatRoomUser> roomUsers = users.stream()
                .map(user -> ChatRoomUser.builder()
                        .chatRoom(room)
                        .user(user)
                        .joinedAt(Instant.now())
                        .build())
                .toList();

        chatRoomUserRepository.saveAll(roomUsers);

        return ApiResponse.success(room.getId());
    }

    @Override
    public ApiResponse<?> addUsersToGroup(
            UUID roomId,
            List<UUID> userIds
    ) {

        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() ->
                        new EntityNotFound("Room not found"));

        if (!room.getIsGroup()) {
            throw new IllegalStateException(
                    "This is not a group chat"
            );
        }

        if (userIds == null || userIds.isEmpty()) {
            throw new IllegalArgumentException(
                    "User list cannot be empty"
            );
        }

        Set<UUID> uniqueUserIds = new HashSet<>(userIds);

        List<UUID> existingUserIds =
                chatRoomUserRepository.findUserIdsByRoomId(roomId);

        uniqueUserIds.removeAll(existingUserIds);

        if (uniqueUserIds.isEmpty()) {
            throw new IllegalStateException(
                    "All users are already in group"
            );
        }

        List<User> users = userRepository.findAllById(uniqueUserIds);

        if (users.size() != uniqueUserIds.size()) {
            throw new EntityNotFound("User not found");
        }

        List<ChatRoomUser> roomUsers = users.stream()
                .map(user -> ChatRoomUser.builder()
                        .chatRoom(room)
                        .user(user)
                        .joinedAt(Instant.now())
                        .build())
                .toList();

        chatRoomUserRepository.saveAll(roomUsers);

        return ApiResponse.success("Add members success");
    }

    @Override
    public ApiResponse<?> getMyChats(
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);
        Authentication auth =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String username = auth.getName();
        User currentUser = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new EntityNotFound("User not found") );
        Page<ChatRoomResponse> chats =
                chatRoomUserRepository.getUserChats(
                        currentUser.getId(),
                        pageable
                );

        return ApiResponse.success(chats);
    }
}