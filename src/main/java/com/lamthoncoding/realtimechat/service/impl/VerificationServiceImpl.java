package com.lamthoncoding.realtimechat.service.impl;


import com.lamthoncoding.realtimechat.constraint.EStatus;
import com.lamthoncoding.realtimechat.entity.User;
import com.lamthoncoding.realtimechat.entity.VerificationToken;
import com.lamthoncoding.realtimechat.repository.UserRepository;
import com.lamthoncoding.realtimechat.repository.VerificationTokenRepository;
import com.lamthoncoding.realtimechat.service.VerificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {
    @Value("${email.expirationMin}")
    private int expirationMin;

    private final VerificationTokenRepository tokenRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public String createToken(User user) {
        String token = UUID.randomUUID().toString();
        
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .expiryDate(LocalDateTime.now().plusMinutes(expirationMin))
                .user(user)
                .build();
        tokenRepository.save(verificationToken);
        
        return token;
    }

    @Override
    @Transactional
    public String validateToken(String token) {
        VerificationToken vToken = tokenRepository.findByToken(token);

        if (vToken == null) {
            return "INVALID";
        }

        if (vToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(vToken);
            return "EXPIRED";
        }


        User user = vToken.getUser();
        user.setStatus(EStatus.ACTIVE);
        userRepository.save(user);
        
        tokenRepository.delete(vToken);
        
        return "SUCCESS";
    }

    @Override
    @Transactional
    public void deleteToken(String token) {
        tokenRepository.deleteByToken(token);
    }

    @Scheduled(cron = "0 0 2 * * ?")
    @Override
    public void cleanupExpiredTokens() {
        tokenRepository.deleteByExpiryDateBefore(
                LocalDateTime.now()
        );
    }
}