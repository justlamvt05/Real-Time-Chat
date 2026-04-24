package com.lamthoncoding.realtimechat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageKafkaDto {

    private UUID id;

    private UUID chatRoomId;

    private UUID senderId;

    private String username;

    private String content;

    private String type;

    private String fileUrl;

    private Boolean deleted;

    private Boolean edited;

    private UUID replyToId;
}