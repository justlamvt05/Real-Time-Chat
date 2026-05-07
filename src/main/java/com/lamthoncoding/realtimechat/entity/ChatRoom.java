package com.lamthoncoding.realtimechat.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tbl_chat_room")
@Getter
@Setter
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name; // null if 1-1

    private Boolean isGroup;

    private String avatar;


    @ManyToOne
    private User createdBy;

    @OneToOne
    private Message lastMessage;
}
