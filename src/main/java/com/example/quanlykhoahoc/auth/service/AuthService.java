package com.example.quanlykhoahoc.auth.service;

import com.example.quanlykhoahoc.auth.dto.AuthResponse;
import com.example.quanlykhoahoc.auth.dto.LoginRequest;
import com.example.quanlykhoahoc.auth.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
