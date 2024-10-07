package com.example.nagoyameshi.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.nagoyameshi.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    
    public Page<User> findByNameContainingOrFuriganaContainingOrEmailContaining(String nameKeyword, String furiganaKeyword, String emailKeyword, Pageable pageable);
	public User findByName(String name);
	
	@Query("SELECT COUNT(u) FROM User u")
	Integer countAllUsers();
	
	@Query("SELECT COUNT(u) FROM User u WHERE u.paid = false")
	Integer countFreeMembership();
	
	@Query("SELECT COUNT(u) FROM User u WHERE u.paid = true")
	Integer countPaidMembership();
}