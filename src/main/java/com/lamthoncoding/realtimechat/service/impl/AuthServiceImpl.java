package com.lamthoncoding.realtimechat.service.impl;


import com.lamthoncoding.realtimechat.constraint.AuthProvider;
import com.lamthoncoding.realtimechat.constraint.EStatus;
import com.lamthoncoding.realtimechat.dto.UserDto;
import com.lamthoncoding.realtimechat.entity.Role;
import com.lamthoncoding.realtimechat.entity.User;
import com.lamthoncoding.realtimechat.entity.VerificationToken;
import com.lamthoncoding.realtimechat.exception.handlers.EntityNotFound;
import com.lamthoncoding.realtimechat.exception.handlers.InvalidInputException;
import com.lamthoncoding.realtimechat.mapper.UserMapper;
import com.lamthoncoding.realtimechat.payload.request.LoginRequest;
import com.lamthoncoding.realtimechat.payload.request.RegisterRequest;
import com.lamthoncoding.realtimechat.payload.response.ApiCode;
import com.lamthoncoding.realtimechat.payload.response.ApiResponse;
import com.lamthoncoding.realtimechat.repository.RoleRepository;
import com.lamthoncoding.realtimechat.repository.UserRepository;
import com.lamthoncoding.realtimechat.repository.VerificationTokenRepository;
import com.lamthoncoding.realtimechat.service.AuthService;
import com.lamthoncoding.realtimechat.service.UserService;
import com.lamthoncoding.realtimechat.service.VerificationService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final VerificationTokenRepository verificationTokenRepository;
    private final VerificationService verificationService;
    private final UserService userService;
    private final RoleRepository roleRepository;


    @Override
    public UserDto login(LoginRequest loginRequest) {
        log.info("Login request: {}", loginRequest.getUsername());
        User user = userRepository.findByUsername(
                loginRequest.getUsername()).orElseThrow(()
                -> new EntityNotFound("Invalid username or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new EntityNotFound("Invalid username or password.");
        }

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public ApiResponse<?> register(RegisterRequest request) throws MessagingException {
        Role r = roleRepository.findByName("ROLE_CUSTOMER").orElseThrow(() -> new EntityNotFound("Invalid role"));
        validateRegisterRequest(request);
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .fullName(request.getFullName())
                .displayName(request.getUsername())
                .timeZone("Asia/Kolkata")
                // Todo: avarta
                .role(r)
                .phone(request.getPhone())
                .authProvider(AuthProvider.GOOGLE)
                .status(EStatus.INACTIVE)
                .build();
        User savedUser = userRepository.save(user);

        String token =  verificationService.createToken(savedUser);


        userService.sendVerificationEmail(savedUser,token);
        return ApiResponse.success("Register successfully. Check your email if activation mail not sent");
    }

    @Override
    public ApiResponse<?> confirm(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

        if (verificationToken == null || verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ApiResponse.error(ApiCode.CONFLICT,"Token is invalid or expired");
        }
        User user = verificationToken.getUser();
        user.setStatus(EStatus.ACTIVE);
        userRepository.save(user);

        return ApiResponse.success("Your account is activated");
    }
    private void validateRegisterRequest(RegisterRequest request) {

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidInputException("Password and Confirm Password do not match");
        }

        User existingUser = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (existingUser != null) {

            if (existingUser.getStatus() == EStatus.ACTIVE) {
                throw new InvalidInputException("Email already exists");
            }

        }

        User existingUsername = userRepository.findByUsername(request.getUsername()).orElse(null);

        if (existingUsername != null && existingUsername.getStatus() == EStatus.ACTIVE) {
            throw new InvalidInputException("Username already exists");
        }
        User existingPhone = userRepository.findByPhone(request.getPhone()).orElse(null);

        if (existingPhone != null && existingPhone.getStatus() == EStatus.ACTIVE) {
            throw new InvalidInputException("Phone number already exists");
        }

    }


}
