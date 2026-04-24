package com.lamthoncoding.realtimechat.service;

import com.lamthoncoding.realtimechat.dto.MessageDTO;
import com.lamthoncoding.realtimechat.entity.Message;
import com.lamthoncoding.realtimechat.payload.response.ApiResponse;
import com.lamthoncoding.realtimechat.payload.response.MessageResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface MessageService {
//    Message sendMessage(String senderUsername, MessageDTO dto);
ApiResponse<Page<MessageResponse>> getMessages(UUID chatRoomId, int page, int size);

}
