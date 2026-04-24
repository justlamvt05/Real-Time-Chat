package com.lamthoncoding.realtimechat.controller;



import com.lamthoncoding.realtimechat.payload.request.AdminUserRequestInsert;
import com.lamthoncoding.realtimechat.payload.request.AdminUserRequestUpdate;
import com.lamthoncoding.realtimechat.payload.response.ApiResponse;
import com.lamthoncoding.realtimechat.service.AdminService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<?>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "username") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status
    ) {
        return ResponseEntity.ok(
                adminService.getUsers(page, size, sortBy, direction, keyword, status)
        );
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<?>> getUserDetail(@PathVariable UUID id) {
        return ResponseEntity.ok(
                adminService.findUser(id)
        );
    }

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<?>> addUser(
            @Valid @RequestBody AdminUserRequestInsert request
    ) {
        return ResponseEntity.ok(adminService.addUser(request));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse<?>> updateUser(
            @PathVariable String id,
            @Valid @RequestBody AdminUserRequestUpdate request
    ) {
        return ResponseEntity.ok(adminService.updateUser(UUID.fromString(id), request));
    }

    @PatchMapping("/users/{id}/toggle")
    public ResponseEntity<ApiResponse<?>> toggleUser(@PathVariable UUID id) {
        return ResponseEntity.ok(adminService.toggleUser(id));
    }

    @GetMapping("/users/export")
    public void exportExcel(HttpServletResponse response) throws IOException {
        adminService.exportUsersToExcel(response);
    }
}