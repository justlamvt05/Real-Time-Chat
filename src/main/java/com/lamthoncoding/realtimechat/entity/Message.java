package com.lamthoncoding.realtimechat.entity;

import com.lamthoncoding.realtimechat.constraint.MessageStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "message")
@Getter
@Setter
public class Message extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private ChatRoom chatRoom;

    @ManyToOne
    private User sender;

    private String content;

    private String type; // TEXT, IMAGE, FILE

    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    private String fileUrl;

    @ElementCollection
    @CollectionTable(
            name = "tbl_message_images",
            joinColumns = @JoinColumn(name = "message_id")
    )
    @Column(name = "image_url")
    private List<String> images;

    private Boolean deleted = false;


    private Boolean edited = false;

    @ManyToOne
    private Message replyTo;
}
