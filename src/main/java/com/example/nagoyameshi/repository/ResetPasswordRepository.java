package com.example.nagoyameshi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.User;

public interface ResetPasswordRepository extends JpaRepository<User, Integer> {
}
