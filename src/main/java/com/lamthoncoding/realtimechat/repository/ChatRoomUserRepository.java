package com.lamthoncoding.realtimechat.repository;

import com.lamthoncoding.realtimechat.entity.ChatRoomUser;
import com.lamthoncoding.realtimechat.payload.response.ChatRoomResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, UUID> {
    @Query("""
    SELECT cru.chatRoom.id
    FROM ChatRoomUser cru
    WHERE cru.user.id IN :userIds
    GROUP BY cru.chatRoom.id
    HAVING COUNT(cru.user.id) = :size
    """)
    Optional<UUID> findPrivateRoom(List<UUID> userIds, long size);

    @Query("""
    SELECT u.username FROM ChatRoomUser cu
    JOIN cu.user u
    WHERE cu.chatRoom.id = :chatRoomId
    """)
    List<String> findUsernamesByChatRoomId(UUID chatRoomId);

    @Query("""
        SELECT new com.lamthoncoding.realtimechat.payload.response.ChatRoomResponse(
            cr.id,

            CASE
                WHEN cr.isGroup = true THEN cr.name
                ELSE (
                    SELECT u.username
                    FROM ChatRoomUser cru2
                    JOIN cru2.user u
                    WHERE cru2.chatRoom.id = cr.id
                    AND u.id <> :userId
                )
            END,

            cr.isGroup
        )
        FROM ChatRoomUser cru
        JOIN cru.chatRoom cr
        WHERE cru.user.id = :userId
    """)
    Page<ChatRoomResponse> getUserChats(UUID userId, Pageable pageable);

}
