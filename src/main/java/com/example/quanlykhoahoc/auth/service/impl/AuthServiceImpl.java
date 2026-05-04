package com.example.quanlykhoahoc.auth.service.impl;

import com.example.quanlykhoahoc.auth.dto.AuthResponse;
import com.example.quanlykhoahoc.auth.dto.LoginRequest;
import com.example.quanlykhoahoc.auth.dto.RegisterRequest;
import com.example.quanlykhoahoc.auth.entity.Role;
import com.example.quanlykhoahoc.auth.entity.User;
import com.example.quanlykhoahoc.auth.repository.RoleRepository;
import com.example.quanlykhoahoc.auth.repository.UserRepository;
import com.example.quanlykhoahoc.auth.service.AuthService;
import com.example.quanlykhoahoc.common.exception.BadRequestException;
import com.example.quanlykhoahoc.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

// Implementation xử lý nghiệp vụ đăng ký/đăng nhập.
// Lưu ý: class này tự kiểm tra password (passwordEncoder.matches) và trả về AuthResponse.
// Authentication (HTTP Basic) được cấu hình ở SecurityConfig.
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    // Role mặc định gán cho user khi đăng ký
    private static final String DEFAULT_ROLE = "STUDENT";

    // Repository thao tác với DB
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    // Dùng để hash/verify mật khẩu (BCrypt)
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Chuẩn hoá email để tránh trùng lặp do khác hoa/thường hoặc khoảng trắng
        String email = normalizeEmail(request.getEmail());

        // Check email đã tồn tại chưa
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email already exists");
        }

        // Tìm role mặc định; nếu chưa có thì tạo mới trong DB
        Role studentRole = roleRepository.findByRoleNameIgnoreCase(DEFAULT_ROLE)
                .orElseGet(() -> roleRepository.save(Role.builder().roleName(DEFAULT_ROLE).build()));

        // Tạo entity User từ request
        User user = User.builder()
                .email(email)
                // Lưu password dạng hash, không lưu plain text
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .isActive(true)
                .build();

        // Gán role cho user
        user.getRoles().add(studentRole);

        // Persist xuống DB
        User savedUser = userRepository.save(user);
        // Convert entity -> DTO trả về cho client
        return toAuthResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        // Chuẩn hoá email trước khi query
        String email = normalizeEmail(request.getEmail());

        // Tìm user theo email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Không cho login nếu tài khoản bị inactive
        if (Boolean.FALSE.equals(user.getIsActive())) {
            throw new BadRequestException("User is inactive");
        }

        // Verify password: so sánh password client gửi lên với hash trong DB
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Invalid credentials");
        }

        // Trả DTO (không trả passwordHash)
        return toAuthResponse(user);
    }

    private AuthResponse toAuthResponse(User user) {
        // Convert danh sách Role entity -> Set<String>
        Set<String> roles = user.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());

        return AuthResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .avatarUrl(user.getAvatarUrl())
                .roles(roles)
                .build();
    }

    private String normalizeEmail(String email) {
        // Chuẩn hoá email về lowercase và trim
        return email == null ? null : email.trim().toLowerCase();
    }
}
