package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.nagoyameshi.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer>{
	List<Review> findByRestaurantId(Integer restaurantId);
	Page<Review> findByRestaurantIdOrderByScoreDesc(Integer restaurantId, Pageable pageable);
	
	@Query("SELECT AVG(r.score) FROM Review r WHERE r.restaurant.id = :restaurantId")
    Double findAverageScoreByRestaurantId(Integer restaurantId);
}
