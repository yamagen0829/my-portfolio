package com.example.nagoyameshi.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReviewForm;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.repository.ReviewRepository;
import com.example.nagoyameshi.repository.UserRepository;

import jakarta.validation.Valid;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    
    public ReviewService (ReviewRepository reviewRepository, UserRepository userRepository, RestaurantRepository restaurantRepository) {
    	this.reviewRepository = reviewRepository;
    	this.userRepository = userRepository;
    	this.restaurantRepository = restaurantRepository;
    }
    
    public void saveReview(Integer restaurantId, ReviewForm reviewForm, String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restaurantId);

        if (optionalUser.isPresent() && optionalRestaurant.isPresent()) {
            User user = optionalUser.get();
            Restaurant restaurant = optionalRestaurant.get();

            Review review = new Review();
            review.setRestaurant(restaurant);
            review.setScore(reviewForm.getScore());
            review.setContent(reviewForm.getContent());
            review.setUser(user);

            reviewRepository.save(review);
        } else {
            throw new IllegalStateException("ログインユーザーまたはレストランが見つかりません。");
        }
    }

	public void updateReview(Integer reviewId, @Valid ReviewForm reviewForm, String email) {
		Optional<User> optionalUser = userRepository.findByEmail(email);
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);

        if (optionalUser.isPresent() && optionalReview.isPresent()) {
            User user = optionalUser.get();
            Review review = optionalReview.get();

            review.setScore(reviewForm.getScore());
            review.setContent(reviewForm.getContent());
           
            reviewRepository.save(review);
        } else {
            throw new IllegalStateException("ログインユーザーまたはレビューが見つかりません。");
        }
	}    
}
