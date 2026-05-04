package com.example.quanlykhoahoc.auth.entity;

import jakarta.persistence.*;
import lombok.*;

// Entity ánh xạ tới bảng Roles.
// Dùng để phân quyền/nhóm quyền cho user (ví dụ: STUDENT, ADMIN...).
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Roles")
public class Role {

    // Khoá chính
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoleId")
    private Integer roleId;

    // Tên role (unique)
    @Column(name = "RoleName", nullable = false, unique = true, length = 50)
    private String roleName;
}
