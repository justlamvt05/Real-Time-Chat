package com.lamthoncoding.realtimechat.service;

import com.lamthoncoding.realtimechat.dto.MessageKafkaDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "chat-message-topic", groupId = "realtime-chat-group")
    public void consume(MessageKafkaDto dto) {
        log.info("consume message: {}", dto.getContent());
        messagingTemplate.convertAndSend(
                "/topic/room/" + dto.getChatRoomId(),
                dto
        );
    }
}