package com.example.quanlykhoahoc.auth.repository;

import com.example.quanlykhoahoc.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email); //Tìm xem có email không (Optional tránh bị null khi không tìm thấy dữ liệu)

    boolean existsByEmail(String email); //Kiểm tra xem email có bị trùng không
}
