package com.example.quanlykhoahoc.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

//Sau khi đăng ký/đăng nhập thành công client sẽ nhận được ngay những thông tin này

@Getter
@Builder
@AllArgsConstructor
public class AuthResponse {

    private final Integer userId;

    private final String fullName;

    private final String email;

    private final String phone;

    private final String avatarUrl; // Link avatar (nếu có)

    private final Set<String> roles; // Danh sách role của user (vd: STUDENT, ADMIN...)
}
