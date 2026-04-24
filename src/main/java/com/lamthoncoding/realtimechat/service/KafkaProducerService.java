package com.lamthoncoding.realtimechat.service;

import com.lamthoncoding.realtimechat.entity.Message;


public interface KafkaProducerService {
    void sendMessage(Message message);

}