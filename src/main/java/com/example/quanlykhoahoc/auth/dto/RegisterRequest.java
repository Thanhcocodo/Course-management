package com.example.quanlykhoahoc.auth.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RegisterRequest {

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")  // Phải đúng dạng email (có @, có .com, ...)
    private String email;

    @NotBlank(message = "Password không được để trống")
    @Size(min = 6, message = "Password quá ngắn")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>?]).+$",
            message = "Mật khẩu phải chứa ít nhất 1 chữ hoa và 1 ký tự đặc biệt"
    )
    private String password;

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(
            regexp = "^0[0-9]{9}$",
            message = "Số điện thoại phải bắt đầu bằng số 0 và có đúng 10 số"
    )
    private String phone;
}
