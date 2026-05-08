package com.example.quanlykhoahoc.security;

import com.example.quanlykhoahoc.auth.entity.User;
import com.example.quanlykhoahoc.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service //đăng ký bean để Spring quản lý
@RequiredArgsConstructor //Lombok tự tạo constructor cho final field
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException{
        String email = normalizeEmail(username);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng"));

        return UserPrincipal.fromUser(user);
    }

    private String normalizeEmail(String email){
        return email == null ? null : email.trim().toLowerCase();
    }
}
