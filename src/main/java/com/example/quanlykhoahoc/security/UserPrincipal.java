package com.example.quanlykhoahoc.security;

import com.example.quanlykhoahoc.auth.entity.Role;
import com.example.quanlykhoahoc.auth.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

//Cung cấp thông tin user cho Spring Security để kiểm tra đăng nhập và phân quyền

//class này phải tuân theo chuẩn của Spring Security (Spring chỉ làm việc với UserDetails)
//UserDetails đại diện cho thông tin user dưới dạng mà Spring Security có thể xử lý (UserDetails chuyển đổi thông tin cho Spring Security hiểu)
public class UserPrincipal implements UserDetails {

    private final Integer userId;
    private final String email;
    private final String passwordHash;
    private final boolean active;
    private final Set<GrantedAuthority> authorities;

    //Tạo object (tạo bản sao lưu của user để security dùng)
    public UserPrincipal(Integer userId, String email, String passwordHash, boolean active, Set<GrantedAuthority> authorities){
        this.userId = userId;
        this.email = email;
        this.passwordHash = passwordHash;
        this.active = active;
        this.authorities = authorities;
    }

    public static UserPrincipal fromUser(User user){
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(Role::getRoleName)
                .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName))
                .collect(Collectors.toSet());

        return new UserPrincipal(
                user.getUserId(),
                user.getEmail(),
                user.getPasswordHash(),
                Boolean.TRUE.equals(user.getIsActive()),
                authorities
        );
    }
    //lấy ID từ token
    public Integer getUserId() {
        return userId;
    }

    //Trả về danh sách quyền role
    //Dùng cái này để check quyền, phân quyền API
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    //Trả về mật khẩu đã mã hóa trong DB
    //Dùng để so sánh với password user nhập khi login
    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return active; }
}
