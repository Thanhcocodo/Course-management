package com.example.quanlykhoahoc.auth.dto;

import lombok.*;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
public class AuthResponse {

    private final Integer userId;
    private final String fullName;
    private final String email;
    private final String phone;
    private final String avatarUrl;
    private final Set<String> roles;  // Danh sách role của user (vd: STUDENT, ADMIN...)

}
