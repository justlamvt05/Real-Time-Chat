package com.lamthoncoding.realtimechat.service;

import com.lamthoncoding.realtimechat.dto.MessageKafkaDto;


public interface KafkaConsumerService {
    void consume(MessageKafkaDto dto);

}