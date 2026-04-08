package com.lamthoncoding.realtimechat.repository;


import com.lamthoncoding.realtimechat.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, UUID> {
    VerificationToken findByToken(String token);

    void deleteByToken(String token);

    void deleteByExpiryDateBefore(LocalDateTime time);
}
