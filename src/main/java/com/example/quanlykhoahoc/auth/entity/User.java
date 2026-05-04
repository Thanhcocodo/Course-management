package com.example.quanlykhoahoc.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

// Entity ánh xạ tới bảng Users.
// Chứa thông tin người dùng + quan hệ roles (nhiều-nhiều qua bảng UserRoles).
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserId")
    private Integer userId;

    @Column(name = "FullName", length = 100)
    private String fullName;

    @Column(name = "Email", length = 100, unique = true)
    private String email;

    // Mật khẩu dạng hash (không lưu plain text)
    @Column(name = "PasswordHash", length = 255)
    private String passwordHash;

    @Column(name = "Phone", length = 10)
    private String phone;

    @Column(name = "AvatarUrl", length = 255)
    private String avatarUrl;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @Column(name = "IsActive")
    private Boolean isActive;

    // Quan hệ N-N: 1 user có nhiều role, 1 role có thể thuộc nhiều user.
    // FetchType.EAGER: khi load User sẽ load luôn roles (tiện cho auth nhưng có thể tốn query).
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "UserRoles",
            joinColumns = @JoinColumn(name = "UserId"),
            inverseJoinColumns = @JoinColumn(name = "RoleId")
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        // Tự set giá trị mặc định trước khi insert lần đầu
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (isActive == null) {
            isActive = true;
        }
    }
}
