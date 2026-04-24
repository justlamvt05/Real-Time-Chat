package com.lamthoncoding.realtimechat.service;

import com.lamthoncoding.realtimechat.payload.request.AdminUserRequestInsert;
import com.lamthoncoding.realtimechat.payload.request.AdminUserRequestUpdate;
import com.lamthoncoding.realtimechat.payload.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

public interface AdminService {

    ApiResponse<?> getUsers(int page, int size, String sortBy, String direction, String keyword, String status);

    ApiResponse<?> addUser(AdminUserRequestInsert request);

    ApiResponse<?> updateUser(UUID userId, AdminUserRequestUpdate request);

    ApiResponse<?> findUser(UUID userId);

    ApiResponse<?> toggleUser(UUID userId);

    void exportUsersToExcel(HttpServletResponse response) throws IOException;
}