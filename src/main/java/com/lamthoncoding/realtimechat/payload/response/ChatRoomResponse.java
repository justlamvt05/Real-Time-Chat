package com.lamthoncoding.realtimechat.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ChatRoomResponse {

    private UUID chatId;

    private String chatName;

    private Boolean isGroup;
}