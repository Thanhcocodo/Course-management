package com.example.quanlykhoahoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Entry point của ứng dụng Spring Boot.
// @SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan
@SpringBootApplication
public class QuanLyKhoaHocApplication {

    public static void main(String[] args) {
        // Khởi động Spring context, scan bean và start embedded server (Tomcat/Jetty tuỳ dependency)
        SpringApplication.run(QuanLyKhoaHocApplication.class, args);
    }

}
