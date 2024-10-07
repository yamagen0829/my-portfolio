package com.example.nagoyameshi.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.Restaurant;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
	Optional<Favorite> findByUserIdAndRestaurantId(Integer userId, Integer restaurantId);
	
	boolean existsByUserIdAndRestaurantId(Integer userId, Integer restaurantId);
	
	@Query("SELECT f.restaurant FROM Favorite f WHERE f.user.id = :userId")
	Page<Restaurant> findFavoritesByUserId(@Param("userId") Integer userId, Pageable pageable);

}
