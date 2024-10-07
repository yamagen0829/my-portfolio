package com.example.nagoyameshi.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
     Optional<Category> findByName(String name);
     public Page<Category> findByNameLike(String keyword, Pageable pageable);
}
