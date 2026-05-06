package com.lamthoncoding.realtimechat.service.impl;


import com.lamthoncoding.realtimechat.entity.ChatRoom;
import com.lamthoncoding.realtimechat.entity.Message;
import com.lamthoncoding.realtimechat.entity.User;
import com.lamthoncoding.realtimechat.exception.handlers.EntityNotFound;
import com.lamthoncoding.realtimechat.payload.request.ChatMessageRequest;
import com.lamthoncoding.realtimechat.repository.ChatRoomRepository;
import com.lamthoncoding.realtimechat.repository.ChatRoomUserRepository;
import com.lamthoncoding.realtimechat.repository.MessageRepository;
import com.lamthoncoding.realtimechat.repository.UserRepository;
import com.lamthoncoding.realtimechat.service.ChatRoomService;
import com.lamthoncoding.realtimechat.service.KafkaProducerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final KafkaProducerService kafkaProducerService;

    @Override
    public void handleMessage(ChatMessageRequest request, Principal principal) {

        String username = principal.getName();

        User currentUser = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new EntityNotFound("User Not Found"));

        ChatRoom room = chatRoomRepository
                .findById(request.getChatId())
                .orElseThrow(() -> new EntityNotFound("Chat Room Not Found"));

        if ((request.getImage() == null || request.getImage().isEmpty())
                && (request.getContent() == null || request.getContent().isBlank())
                && (request.getFile() != null && !request.getFile().isEmpty())) {

            for (String fileUrl : request.getFile()) {
                Message fileMsg = new Message();
                fileMsg.setChatRoom(room);
                fileMsg.setSender(currentUser);
                fileMsg.setFileUrl(fileUrl);
                fileMsg.setType("FILE");

                Message saved = messageRepository.save(fileMsg);
                kafkaProducerService.sendMessage(saved);
            }

            return;
        }

        Message message = new Message();
        message.setContent(request.getContent());
        message.setChatRoom(room);
        message.setSender(currentUser);
        message.setImages(request.getImage());

        message.setType(
                request.getImage() != null && !request.getImage().isEmpty()
                        ? "IMAGE"
                        : "TEXT"
        );

        Message saved = messageRepository.save(message);
        kafkaProducerService.sendMessage(saved);
    }



}
