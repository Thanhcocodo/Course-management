package com.example.quanlykhoahoc.auth.service;

import com.example.quanlykhoahoc.auth.dto.AuthResponse;
import com.example.quanlykhoahoc.auth.dto.LoginRequest;
import com.example.quanlykhoahoc.auth.dto.RegisterRequest;

// Interface định nghĩa nghiệp vụ xác thực.
// Controller gọi vào đây, còn implementation sẽ xử lý logic và thao tác DB thông qua repository.
public interface AuthService {
    // Đăng ký tài khoản mới
    AuthResponse register(RegisterRequest request);
    // Đăng nhập: kiểm tra thông tin và trả về thông tin user
    AuthResponse login(LoginRequest request);
}
