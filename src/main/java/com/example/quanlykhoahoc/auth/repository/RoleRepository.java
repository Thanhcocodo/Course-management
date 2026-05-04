package com.example.quanlykhoahoc.auth.repository;

import com.example.quanlykhoahoc.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Repository thao tác với bảng Roles (entity Role).
public interface RoleRepository extends JpaRepository<Role, Integer> {
    // Tìm role theo tên (không phân biệt hoa/thường)
    Optional<Role> findByRoleNameIgnoreCase(String roleName);
}
