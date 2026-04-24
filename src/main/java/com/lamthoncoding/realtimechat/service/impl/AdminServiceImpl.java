package com.lamthoncoding.realtimechat.service.impl;

import com.lamthoncoding.realtimechat.constraint.EStatus;
import com.lamthoncoding.realtimechat.dto.UserDto;
import com.lamthoncoding.realtimechat.entity.Role;
import com.lamthoncoding.realtimechat.entity.User;
import com.lamthoncoding.realtimechat.exception.handlers.EntityNotFound;
import com.lamthoncoding.realtimechat.mapper.UserMapper;
import com.lamthoncoding.realtimechat.payload.request.AdminUserRequestInsert;
import com.lamthoncoding.realtimechat.payload.request.AdminUserRequestUpdate;
import com.lamthoncoding.realtimechat.payload.response.ApiResponse;
import com.lamthoncoding.realtimechat.repository.RoleRepository;
import com.lamthoncoding.realtimechat.repository.UserRepository;
import com.lamthoncoding.realtimechat.service.AdminService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public ApiResponse<?> getUsers(int page, int size, String sortBy, String direction
            , String keyword, String status) {
        log.info("page: {}", page);
        log.info("size: {}", size);
        log.info("sortBy: {}", sortBy);
        log.info("direction: {}", direction);
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        EStatus eStatus = null;
        if (!Objects.isNull(status)) {
            eStatus = EStatus.valueOf(status.toUpperCase());
        }
        Page<UserDto> users = userRepository.findAllUserDto(pageable, keyword, eStatus);

        return ApiResponse.success(users);
    }

    @Override
    public ApiResponse<?> addUser(AdminUserRequestInsert request) {
        log.info("request: {}", request);
        if (userRepository.existsByUsername(request.getUserName())) {
            throw new IllegalArgumentException("Username already exists");
        }
        Role role = roleRepository.findByName("ROLE_CUSTOMER")
                .orElseThrow(() -> new EntityNotFound("Role not found"));

        User user = User.builder()
                .username(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .phone(request.getPhone())
                .fullName(request.getFullName())
                .status(EStatus.ACTIVE)
                .role(role)
                .build();

        userRepository.save(user);

        return ApiResponse.success("User created successfully");

    }

    @Override
    public ApiResponse<?> updateUser(UUID userId, AdminUserRequestUpdate request) {
        log.info("userId: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFound("User not found"));

        user.setFullName(request.getFullName());


        userRepository.save(user);

        return ApiResponse.success("User updated successfully");
    }

    @Override
    public ApiResponse<?> findUser(UUID userId) {
        log.info("userId: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFound("User not found"));
        UserDto userDto = userMapper.toDto(user);
        return ApiResponse.success(userDto);
    }

    @Override
    public ApiResponse<?> toggleUser(UUID userId) {
        log.info("Toggle user status, userId: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFound("User not found"));

        // toggle status
        if (user.getStatus() == EStatus.ACTIVE) {
            user.setStatus(EStatus.INACTIVE);
        } else {
            user.setStatus(EStatus.ACTIVE);
        }

        userRepository.save(user);

        return ApiResponse.success(
                "User " + user.getId() + " status changed to " + user.getStatus()
        );
    }


    @Override
    public void exportUsersToExcel(HttpServletResponse response) throws IOException {

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        );
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=users.xlsx"
        );

        List<User> users = userRepository.findAll();


        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Users");

        // ===== Header =====
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "UserId", "Username", "FirstName", "LastName", "Date of Birth", "Address", "Email", "Role", "Status"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // ===== Data =====
        int rowIdx = 1;
        for (User u : users) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(1).setCellValue(u.getUsername());
            row.createCell(2).setCellValue(u.getFullName());
            row.createCell(3).setCellValue(u.getBirthday());
            row.createCell(4).setCellValue(u.getEmail());
            row.createCell(6).setCellValue(u.getRole().getName());
            row.createCell(7).setCellValue(u.getStatus().name());
        }

        // Auto size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write to response
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}