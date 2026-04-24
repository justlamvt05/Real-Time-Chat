package com.lamthoncoding.realtimechat.repository;

import com.lamthoncoding.realtimechat.constraint.EStatus;
import com.lamthoncoding.realtimechat.dto.UserDto;
import com.lamthoncoding.realtimechat.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("""
    SELECT new com.lamthoncoding.realtimechat.dto.UserDto(
        u.id,
        u.username,
        u.fullName,
        u.email,
        u.phone,
        u.status
    )
    FROM User u
    WHERE
    (:keyword IS NULL OR
        LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
    )
    AND (:status IS NULL OR u.status = :status)
    """)
    Page<UserDto> findAllUserDto(
            Pageable pageable,
            @Param("keyword") String keyword,
            @Param("status") EStatus status
    );
}
