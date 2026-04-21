package com.lamthoncoding.realtimechat.security;

import com.lamthoncoding.realtimechat.dto.UserStatusDTO;
import com.lamthoncoding.realtimechat.repository.UserRepository;
import com.lamthoncoding.realtimechat.service.UserService;
import com.lamthoncoding.realtimechat.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;


    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = accessor.getUser().getName();

        userService.setOnline(username);

        messagingTemplate.convertAndSend("/topic/status",
                new UserStatusDTO(username, "ONLINE"));
        log.info("User connected");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = accessor.getUser().getName();

        userService.setOffline(username);

        messagingTemplate.convertAndSend("/topic/status",
                new UserStatusDTO(username, "OFFLINE"));
        log.info("User disconnected");
    }
}
