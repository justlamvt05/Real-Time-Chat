package com.lamthoncoding.realtimechat.service.impl;

import com.lamthoncoding.realtimechat.dto.MessageDTO;
import com.lamthoncoding.realtimechat.entity.Message;
import com.lamthoncoding.realtimechat.entity.User;
import com.lamthoncoding.realtimechat.entity.ChatRoom;
import com.lamthoncoding.realtimechat.constraint.MessageStatus;
import com.lamthoncoding.realtimechat.payload.response.ApiResponse;
import com.lamthoncoding.realtimechat.payload.response.MessageResponse;
import com.lamthoncoding.realtimechat.repository.ChatRoomRepository;
import com.lamthoncoding.realtimechat.repository.ChatRoomUserRepository;
import com.lamthoncoding.realtimechat.repository.MessageRepository;
import com.lamthoncoding.realtimechat.repository.UserRepository;
import com.lamthoncoding.realtimechat.service.MessageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    @Override
    public ApiResponse<Page<MessageResponse>> getMessages(UUID chatRoomId, int page, int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Message> messages = messageRepository.getMessages(chatRoomId, pageable);

        Page<MessageResponse> response = messages.map(this::mapToResponse);

        return ApiResponse.success(response);
    }

    private MessageResponse mapToResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .content(message.getContent())
                .senderUsername(message.getSender().getUsername())
                .chatRoomId(message.getChatRoom().getId())
                .images(message.getImages())
                .fileUrl(message.getFileUrl())
                .build();
    }


}
