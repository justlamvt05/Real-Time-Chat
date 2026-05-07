package com.lamthoncoding.realtimechat.entity;

import com.lamthoncoding.realtimechat.constraint.RoomRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;
@Entity
@Table(name = "tbl_chat_room_user")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private ChatRoom chatRoom;

    @ManyToOne
    private User user;

    private Instant joinedAt;

    private Instant leftAt;

    private Boolean deleted = false;

    private UUID lastReadMessageId;

    private Instant lastReadAt;

    private Boolean muted = false;

    private Boolean pinned = false;

    @Enumerated(EnumType.STRING)
    private RoomRole role;
}

