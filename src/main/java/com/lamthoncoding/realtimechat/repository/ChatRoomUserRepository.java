package com.lamthoncoding.realtimechat.repository;

import com.lamthoncoding.realtimechat.entity.ChatRoomUser;
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
}
