package com.example.quanlykhoahoc.auth.repository;

import com.example.quanlykhoahoc.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByRoleNameIgnoreCase(String roleName); //Kiểm tra xem có tên role hay không

}
