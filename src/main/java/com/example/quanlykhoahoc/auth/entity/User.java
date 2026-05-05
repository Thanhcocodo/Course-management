package com.example.quanlykhoahoc.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Users") //Đại diện cho bẳng trong database
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserId")
    private Integer userId;

    @Column(name = "FullName", length = 100)
    private String fullName;

    @Column(name = "Email", length = 100, unique = true)
    private String email;

    @Column(name = "PasswordHash", length = 255)
    private String passwordHash;

    @Column(name = "Phone", length = 20)
    private String phone;

    @Column(name = "AvatarUrl", length = 255)
    private String avatarUrl;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @Column(name = "IsActive")
    private Boolean isActive;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "UserRoles",
            joinColumns = @JoinColumn(name = "UserId"),
            inverseJoinColumns = @JoinColumn(name = "RoleId")
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>(); //Gắn quyền cho user

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (isActive == null) {
            isActive = true;
        }
    }
}
