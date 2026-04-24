package com.lamthoncoding.realtimechat.entity;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic chatTopic() {
        return new NewTopic("chat-message-topic", 1, (short) 1);
    }
}