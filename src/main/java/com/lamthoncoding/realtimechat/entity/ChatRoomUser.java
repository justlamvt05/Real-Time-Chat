package com.lamthoncoding.realtimechat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;
@Entity
@Table(name = "tbl_chat_room_user")
@Getter
@Setter
public class ChatRoomUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private ChatRoom chatRoom;

    @ManyToOne
    private User user;

    private Instant joinedAt;
}

