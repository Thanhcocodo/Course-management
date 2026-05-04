package com.example.quanlykhoahoc.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Exception tuỳ biến dùng để biểu diễn lỗi 400 (BAD_REQUEST) ở mức nghiệp vụ.
// Có thể throw ở service/controller khi input không hợp lệ hoặc vi phạm rule.
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
