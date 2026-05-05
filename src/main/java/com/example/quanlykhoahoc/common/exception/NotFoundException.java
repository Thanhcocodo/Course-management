package com.example.quanlykhoahoc.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) //khi API này chạy sẽ trả về HTTP 404
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message){
        super(message);
    }
}
