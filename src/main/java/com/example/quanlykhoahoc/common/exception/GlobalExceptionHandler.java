package com.example.quanlykhoahoc.common.exception;

import com.example.quanlykhoahoc.common.dto.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

// Bắt và chuẩn hoá toàn bộ exception phát sinh từ tầng Controller (và các tầng bên dưới ném lên).
// Mục tiêu:
// - Trả về response có format thống nhất (ApiResponse)
// - Mapping exception -> HTTP status phù hợp
// - Log lỗi đúng mức (warn/error) để dễ debug mà không lộ chi tiết nhạy cảm cho client
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(BadRequestException ex) {
        // Lỗi do input/logic nghiệp vụ không hợp lệ (400)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure(ex.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(NotFoundException ex) {
        // Không tìm thấy tài nguyên (404)
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.failure(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        // Lỗi validate DTO khi dùng @Valid (vd: @NotBlank, @Email...).
        // Gộp tất cả lỗi field thành 1 chuỗi để client hiển thị nhanh.
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining("; "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure(message));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {
        // Lỗi validate trên parameter (vd: @RequestParam/@PathVariable) hoặc validate cấp method.
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure(ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        // Lỗi ràng buộc dữ liệu từ DB (unique, FK, not null...).
        // Log root cause để debug nội bộ, nhưng response trả về message chung để tránh lộ chi tiết DB.
        String rootMessage = getRootCauseMessage(ex);
        log.error("Data integrity violation: {}", rootMessage, ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure("Data integrity violation"));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        // Request body thiếu hoặc JSON sai định dạng/không parse được.
        log.warn("Request body is missing or unreadable: {}", getRootCauseMessage(ex));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure("Request body is missing or invalid"));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        // Client gọi sai HTTP method (vd: GET vào endpoint chỉ cho phép POST).
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiResponse.failure("Method not allowed"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception ex) {
        // Fallback: mọi lỗi chưa được bắt ở các handler phía trên.
        // Luôn log stacktrace để phục vụ điều tra.
        log.error("Unhandled exception", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.failure("Internal server error"));
    }

    private String getRootCauseMessage(Throwable throwable) {
        // Duyệt xuống sâu nhất trong chuỗi cause để lấy message gốc (root cause).
        Throwable root = throwable;
        while (root.getCause() != null && root.getCause() != root) {
            root = root.getCause();
        }
        return root.getMessage();
    }

    private String formatFieldError(FieldError fieldError) {
        // Format lỗi theo dạng: field: message
        String field = fieldError.getField();
        String defaultMessage = fieldError.getDefaultMessage();
        return field + ": " + defaultMessage;
    }
}
