package com.lamthoncoding.realtimechat.controller;


import com.lamthoncoding.realtimechat.dto.UserDto;
import com.lamthoncoding.realtimechat.payload.request.LoginRequest;
import com.lamthoncoding.realtimechat.payload.request.RegisterRequest;
import com.lamthoncoding.realtimechat.payload.response.ApiResponse;
import com.lamthoncoding.realtimechat.payload.response.JwtResponse;
import com.lamthoncoding.realtimechat.security.config.jwt.JwtUtils;
import com.lamthoncoding.realtimechat.service.AuthService;
import com.lamthoncoding.realtimechat.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @PostMapping("/login")
    public ApiResponse<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        UserDto userDto = authService.login(loginRequest);
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken
                        (loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateToken(authentication);
        return ApiResponse.success(
                JwtResponse.builder()
                        .type("Bearer")
                        .token(token)
                        .username(userDto.getUsername())
                        .build());

    }
    @PostMapping("/register")
    public ApiResponse<?> register(
            @Valid @RequestBody RegisterRequest request) throws MessagingException {

        return ApiResponse.success(authService.register(request));
    }
    @GetMapping("/confirm")
    public ApiResponse<?> confirm(@RequestParam String token) {

        return ApiResponse.success(authService.confirm(token));
    }
}
