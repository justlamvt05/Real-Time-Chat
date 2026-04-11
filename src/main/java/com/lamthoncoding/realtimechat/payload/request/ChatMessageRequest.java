package com.lamthoncoding.realtimechat.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ChatMessageRequest {
    private UUID chatId;
    private String content;
}
