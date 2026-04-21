package com.lamthoncoding.realtimechat.entity;

import com.lamthoncoding.realtimechat.constraint.AuthProvider;
import com.lamthoncoding.realtimechat.constraint.EGender;
import com.lamthoncoding.realtimechat.constraint.EStatus;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tbl_user", indexes = {
        @Index(name = "idx_username", columnList = "username"),
        @Index(name = "idx_email", columnList = "email")
})
@Getter
@EntityListeners(AuditingEntityListener.class)
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String username;

    private String fullName;

    private String password;

    private String email;

    private String phone;

    private String timeZone;

    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private EGender gender;

    private String displayName;

    private String avatar;

    boolean online;

    private LocalDateTime lastSeen;

    @Enumerated(EnumType.STRING)
    private EStatus status;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;



}
