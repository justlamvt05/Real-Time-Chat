package com.lamthoncoding.realtimechat.service;


import com.lamthoncoding.realtimechat.entity.User;
import jakarta.mail.MessagingException;
import org.springframework.scheduling.annotation.Scheduled;

public interface UserService {
     void sendVerificationEmail(User user, String token) throws MessagingException;

     void setOnline(String username);

     void setOffline(String username);

     @Scheduled(cron = "0 2 14 * * ?") // 14:02 every day
     void cleanInactiveUsers();
}
