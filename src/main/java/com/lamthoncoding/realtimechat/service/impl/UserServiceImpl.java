package com.lamthoncoding.realtimechat.service.impl;


//import com.lamvt.shcedule.security.JavaMailSenderCustom;

import com.lamthoncoding.realtimechat.entity.User;
import com.lamthoncoding.realtimechat.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JavaMailSender mailSender;

    @Async
    @Override
    public void sendVerificationEmail(User user, String token) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        String recipientAddress = user.getEmail();
        String subject = "";
        String confirmationUrl = "http://localhost:8080/api/auth/confirm?token=" + token;

        SimpleMailMessage email = new SimpleMailMessage();
        helper.setTo(recipientAddress);
        helper.setSubject("Confirm account");
        String htmlContent =
                "<div style='font-family: Arial, sans-serif; line-height: 1.6; color: #333;'>" +
                        "<h2>Welcome to our community!</h2>" +
                        "<p>Thank you for signing up. To complete your registration and activate your account, please click the button below:</p>" +
                        "<div style='margin: 30px 0;'>" +
                        "<a href='" + confirmationUrl + "' style='" +
                        "background-color: #4CAF50; " +
                        "color: white; " +
                        "padding: 12px 25px; " +
                        "text-decoration: none; " +
                        "border-radius: 5px; " +
                        "display: inline-block; " +
                        "font-weight: bold;'>" +
                        "Activate Account" +
                        "</a>" +
                        "</div>" +
                        "<p style='font-size: 0.9em; color: #666;'>" +
                        "Note: This link will expire in 24 hours.<br>" +
                        "If you did not create an account, please ignore this email." +
                        "</p>" +
                        "<hr style='border: 0; border-top: 1px solid #eee; margin: 20px 0;'>" +
                        "<p style='font-size: 0.8em; color: #999;'>Best regards,<br>The Support Team</p>" +
                        "</div>";
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}
