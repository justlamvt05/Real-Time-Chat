package com.lamthoncoding.realtimechat.service.impl;

import com.lamthoncoding.realtimechat.dto.MessageKafkaDto;
import com.lamthoncoding.realtimechat.entity.Message;
import com.lamthoncoding.realtimechat.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerServiceImpl implements KafkaProducerService {

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
        dto.setImages(message.getImages());
        log.info("Images before send Kafka: {}", message.getImages());
        kafkaTemplate.send("chat-message-topic", dto);


    }
}
