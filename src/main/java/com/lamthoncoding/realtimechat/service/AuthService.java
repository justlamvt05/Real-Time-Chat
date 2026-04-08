package com.lamthoncoding.realtimechat.service;


import com.lamthoncoding.realtimechat.dto.UserDto;
import com.lamthoncoding.realtimechat.payload.request.LoginRequest;
import com.lamthoncoding.realtimechat.payload.request.RegisterRequest;
import com.lamthoncoding.realtimechat.payload.response.ApiResponse;
import jakarta.mail.MessagingException;


public interface AuthService {
    UserDto login(LoginRequest loginRequest);
    ApiResponse<?> register(RegisterRequest request) throws MessagingException;

    ApiResponse<?> confirm(String token);
}
