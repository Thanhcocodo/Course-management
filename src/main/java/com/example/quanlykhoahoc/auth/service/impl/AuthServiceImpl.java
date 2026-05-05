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

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String DEFAULT_ROLE = "STUDENT"; //Gắn role mặc định

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder; //Dùng để hash mật khẩu (BCrypt)

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = normalizeEmail(request.getEmail());

        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email này đã tồn tại.");
        }

        Role studentRole = roleRepository.findByRoleNameIgnoreCase(DEFAULT_ROLE)
                .orElseGet(() -> roleRepository.save(Role.builder().roleName(DEFAULT_ROLE).build()));

        User user = User.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .isActive(true)
                .build();

        user.getRoles().add(studentRole);

        User savedUser = userRepository.save(user);
        return toAuthResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String email = normalizeEmail(request.getEmail());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));

        if (Boolean.FALSE.equals(user.getIsActive())) {
            throw new BadRequestException("Tài khoản đã bị vô hiệu hóa");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Thông tin đăng nhập không hợp lệ");
        }

        return toAuthResponse(user);
    }

    private AuthResponse toAuthResponse(User user) {
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
        return email == null ? null : email.trim().toLowerCase();
    }
}
