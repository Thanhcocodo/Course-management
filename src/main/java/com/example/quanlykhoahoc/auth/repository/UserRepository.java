package com.example.quanlykhoahoc.auth.repository;

import com.example.quanlykhoahoc.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Repository thao tác với bảng Users (entity User).
// Kế thừa JpaRepository để có sẵn CRUD + paging/sorting.
public interface UserRepository extends JpaRepository<User, Integer> {
    // Tìm user theo email (dùng cho login)
    Optional<User> findByEmail(String email);
    // Check nhanh email đã tồn tại chưa (dùng cho register)
    boolean existsByEmail(String email);
}
