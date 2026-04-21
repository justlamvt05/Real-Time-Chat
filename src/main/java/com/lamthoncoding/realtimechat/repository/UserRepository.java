package com.lamthoncoding.realtimechat.repository;

import com.lamthoncoding.realtimechat.constraint.EStatus;
import com.lamthoncoding.realtimechat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    @Query("""
    SELECT u FROM User u
    WHERE u.status = :status AND u.username = :username
""")
    Optional<User> findByUsernameAndStatus(String username, EStatus status);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<User> findByPhone(String phone);

    @Modifying
    @Query("""
    DELETE FROM User u
    WHERE u.status = 'INACTIVE'
    """)
    void deleteInactiveUsers();


    @Modifying
    @Query("""
    UPDATE User u 
    SET u.online = true 
    WHERE u.username = :username
    """)
    void setOnline(@Param("username") String username);

    @Modifying
    @Query("""
    UPDATE User u 
    SET u.online = true 
    WHERE u.username = :username
    """)
    void setOffline(@Param("username") String username);
}
