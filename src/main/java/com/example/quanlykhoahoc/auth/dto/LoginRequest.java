package com.example.quanlykhoahoc.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

// DTO nhận dữ liệu từ client khi gọi API đăng nhập.
// Các annotation validation sẽ được kích hoạt khi controller dùng @Valid.
@Getter
@Setter
public class LoginRequest {

    @Email(message = "Email không đúng định dạng!")   // Phải đúng dạng email (có @, có .com, ...)
    @NotBlank(message = "Email không được để trống")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
}
