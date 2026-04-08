package com.lamthoncoding.realtimechat.service;


import com.lamthoncoding.realtimechat.entity.User;
import org.springframework.scheduling.annotation.Scheduled;

public interface VerificationService {
    String createToken(User user);
    String validateToken(String token);
    void deleteToken(String token);

    @Scheduled(cron = "0 0 2 * * ?") // 2AM every day
    void cleanupExpiredTokens();
}