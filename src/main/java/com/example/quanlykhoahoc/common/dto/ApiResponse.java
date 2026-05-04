package com.example.quanlykhoahoc.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

// DTO chuẩn hoá response trả về cho client.
// Mọi API nên trả về cùng một format để frontend dễ parse:
// - success: true/false
// - data: dữ liệu trả về (nếu thành công)
// - message: thông điệp cho người dùng / mô tả lỗi
@Getter
@Builder
@AllArgsConstructor
public class ApiResponse<T> {
    private final boolean success;
    private final T data;
    private final String message;

    public static <T> ApiResponse<T> success(T data, String message) {
        // Factory method tạo response thành công
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> failure(String message) {
        // Factory method tạo response thất bại
        return ApiResponse.<T>builder()
                .success(false)
                .data(null)
                .message(message)
                .build();
    }
}
