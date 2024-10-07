package com.example.nagoyameshi.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.repository.FavoriteRepository;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.repository.UserRepository;

@Service
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;  
    private final RestaurantRepository restaurantRepository;  
    private final UserRepository userRepository;  
    
    public FavoriteService(FavoriteRepository favoriteRepository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.favoriteRepository = favoriteRepository;  
        this.restaurantRepository = restaurantRepository;  
        this.userRepository = userRepository;  
    }
    
    public Page<Restaurant> getFavoritesByUserId(Integer userId, Pageable pageable) {
        // ユーザーのお気に入りのレストランを取得するロジック
        return favoriteRepository.findFavoritesByUserId(userId, pageable);
    }
    
    public boolean isFavorite(Integer userId, Integer restaurantId) {
        return favoriteRepository.existsByUserIdAndRestaurantId(userId, restaurantId);
    }
    
    @Transactional
    public void addFavorite(Integer restaurantId, Integer userId) {
    	if (!isFavorite(userId, restaurantId)) {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new RuntimeException("Restaurant not found"));

            Favorite favorite = new Favorite();
            favorite.setUser(user);
            favorite.setRestaurant(restaurant);
            favoriteRepository.save(favorite);
        }
    }

    @Transactional
    public void removeFavorite(Integer restaurantId, Integer userId) {
    	 Optional<Favorite> favorite = favoriteRepository.findByUserIdAndRestaurantId(userId, restaurantId);
         favorite.ifPresent(favoriteRepository::delete);

    }
}
