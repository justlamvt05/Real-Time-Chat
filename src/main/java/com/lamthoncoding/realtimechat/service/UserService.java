package com.lamthoncoding.realtimechat.service;


import com.lamthoncoding.realtimechat.entity.User;
import jakarta.mail.MessagingException;

public interface UserService {
     void sendVerificationEmail(User user, String token) throws MessagingException;
}
