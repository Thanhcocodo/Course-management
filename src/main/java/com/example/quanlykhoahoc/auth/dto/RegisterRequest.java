package com.example.quanlykhoahoc.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

// Validation được kích hoạt khi controller dùng @Valid.
@Getter
@Setter
public class RegisterRequest {

    @Email(message = "Email không đúng định dạng!")   // Phải đúng dạng email (có @, có .com, ...)
    @NotBlank(message = "Email không được để trống")
    @Size(max = 100, message = "Email không được vượt quá 100 ký tự")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6,max = 100, message = "Mật khẩu quá ngắn")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).+$",
            message = "Mật khẩu phải chứa ít nhất 1 chữ hoa và 1 ký tự đặc biệt"
    )
    private String password;

    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 100, message = "Họ tên tối đa 100 ký tự")
    private String fullName;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(
            regexp = "^0[0-9]{9}$",
            message = "Số điện thoại phải bắt đầu bằng số 0 và có đúng 10 số"
    )
    private String phone;
}
