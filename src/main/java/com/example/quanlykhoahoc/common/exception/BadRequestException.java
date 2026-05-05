package com.example.quanlykhoahoc.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) //khi API này chạy sẽ trả về HTTP 400
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message){
        super(message);
    }
}
