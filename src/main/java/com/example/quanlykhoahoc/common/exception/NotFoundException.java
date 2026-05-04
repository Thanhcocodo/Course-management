package com.example.quanlykhoahoc.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Exception tuỳ biến dùng để biểu diễn lỗi 404 (NOT_FOUND) khi không tìm thấy tài nguyên.
// Thường throw ở service khi query theo id/email... nhưng không có dữ liệu.
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
