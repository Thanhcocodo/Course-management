package com.example.quanlykhoahoc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// Cấu hình Spring Security cho toàn bộ ứng dụng.
// Hiện tại dùng HTTP Basic (không dùng form login) và disable CSRF (phù hợp cho REST API).
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt là thuật toán hash mật khẩu phổ biến, có salt và khả năng tăng cost.
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF vì project đang phục vụ REST API (không dùng session cookie)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Cho phép gọi API auth mà không cần đăng nhập
                        .requestMatchers("/api/auth/**").permitAll()
                        // Cho phép truy cập actuator (nếu có bật) để health check
                        .requestMatchers("/actuator/**").permitAll()
                        // Các request còn lại bắt buộc phải authenticate
                        .anyRequest().authenticated()
                )
                // Dùng HTTP Basic Authentication
                .httpBasic(Customizer.withDefaults())
                // Tắt form login mặc định
                .formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
