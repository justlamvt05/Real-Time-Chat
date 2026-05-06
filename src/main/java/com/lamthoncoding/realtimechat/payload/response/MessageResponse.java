package com.lamthoncoding.realtimechat.payload.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

    private UUID id;
    private String content;
    private String senderUsername;
    private UUID chatRoomId;
    private List<String> images;
    private String fileUrl;

}