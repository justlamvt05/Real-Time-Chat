package com.lamthoncoding.realtimechat.service;


import com.lamthoncoding.realtimechat.entity.User;
import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Scheduled;

public interface UserService {
     void sendVerificationEmail(User user, String token) throws MessagingException;

     @Scheduled(cron = "0 2 2 * * ?") // 02:02 every day
     void cleanInactiveUsers();
}
