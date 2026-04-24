package com.lamthoncoding.realtimechat.service;

import com.lamthoncoding.realtimechat.dto.MessageKafkaDto;
import com.lamthoncoding.realtimechat.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, MessageKafkaDto> kafkaTemplate;

    public void sendMessage(Message message) {
        MessageKafkaDto dto = new MessageKafkaDto();

        dto.setId(message.getId());
        dto.setChatRoomId(message.getChatRoom().getId());
        dto.setSenderId(message.getSender().getId());
        dto.setUsername(message.getSender().getUsername());
        dto.setContent(message.getContent());
        dto.setType(message.getType());
        dto.setFileUrl(message.getFileUrl());
        dto.setDeleted(message.getDeleted());
        dto.setEdited(message.getEdited());

        kafkaTemplate.send("chat-message-topic", dto);


    }
}