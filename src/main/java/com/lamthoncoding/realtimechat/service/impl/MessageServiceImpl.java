package com.lamthoncoding.realtimechat.service.impl;

import com.lamthoncoding.realtimechat.entity.Message;
import com.lamthoncoding.realtimechat.exception.handlers.EntityNotFound;
import com.lamthoncoding.realtimechat.payload.response.ApiResponse;
import com.lamthoncoding.realtimechat.payload.response.MessageResponse;
import com.lamthoncoding.realtimechat.repository.MessageRepository;
import com.lamthoncoding.realtimechat.service.MessageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

    @Override
    public ApiResponse<String> deleteMessage(UUID messageId){
        Message message = messageRepository.
                findById(messageId)
                .orElseThrow(()-> new EntityNotFound("Message not found"));
        if (message.getType().equalsIgnoreCase("text")){
            message.setContent(null);
        }else if(message.getType().equalsIgnoreCase("image")){
            message.setImages(null);
        }else {
            message.setFileUrl(null);
        }
        message.setDeleted(true);

        messageRepository.save(message);
        return ApiResponse.success("Message deleted successfully");
    }

    @Override
    public ApiResponse<String> editMessage(UUID messageId, String content){
        Message message = messageRepository.
                findById(messageId)
                .orElseThrow(()-> new EntityNotFound("Message not found"));
        message.setContent(content);
        message.setEdited(true);
        messageRepository.save(message);
        return ApiResponse.success("Message edited successfully");
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
