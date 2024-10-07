package com.example.nagoyameshi.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReviewForm;
import com.example.nagoyameshi.repository.ReviewRepository;
import com.example.nagoyameshi.repository.UserRepository;
import com.example.nagoyameshi.service.RestaurantService;
import com.example.nagoyameshi.service.ReviewService;

@Controller
@RequestMapping("/admin/reviews")
public class AdminReviewController {
	    private final ReviewService reviewService;
	    private final ReviewRepository reviewRepository;
	    private final RestaurantService restaurantService;
	    private final UserRepository userRepository; 
	    
	    public AdminReviewController(ReviewService reviewService, ReviewRepository reviewRepository, RestaurantService restaurantService, UserRepository userRepository) {
	    	this.reviewService = reviewService;
	    	this.reviewRepository = reviewRepository;
	    	this.restaurantService = restaurantService;
	    	this.userRepository = userRepository;
	    }
	 // 現在のユーザーが有料会員かどうかを確認するメソッド
	    private String getCurrentUsername() {
	        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        if (principal instanceof UserDetails) {
	            return ((UserDetails) principal).getUsername();
	        } else {
	            return principal.toString();
	        }
	    }
	    
	    private User getCurrentUser() {
	        String email = getCurrentUsername();
	        Optional<User> optionalUser = userRepository.findByEmail(email);
	        return optionalUser.orElse(null);
	    }
        
	    private boolean isAdmin() {	
	    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();	
	    	if (principal instanceof UserDetails) {	
	    	return ((UserDetails) principal).getAuthorities().stream()	
	    	.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));	
	    	}	
	    	return false;	
	    	}	


	    private boolean isPaidUser() {
	        User user = getCurrentUser();
	        return user != null && Boolean.TRUE.equals(user.getPaid());
	    }

	    @GetMapping("/postReviewForm/{restaurantId}")
	    public String postReviewForm(@PathVariable("restaurantId") Integer restaurantId, Model model) {
	    	if (!isPaidUser() && !isAdmin()) {
	            return "redirect:/user/paid"; // 有料会員ページにリダイレクト
	        }
	    	
	    	model.addAttribute("reviewForm", new ReviewForm());
	        model.addAttribute("restaurant", restaurantService.findById(restaurantId));
	        
	        return "admin/reviews/reviewpost";  
	    }

	    @PostMapping("/submitReview/{restaurantId}")
	    public String submitReview(@PathVariable("restaurantId") Integer restaurantId, @ModelAttribute @Validated ReviewForm reviewForm, BindingResult result, Model model) {
	    	if (!isPaidUser() && !isAdmin()) {
	            return "redirect:/user/paid"; // 有料会員ページにリダイレクト
	        }
	    	
	    	if (result.hasErrors()) {
	            model.addAttribute("restaurant", restaurantService.findById(restaurantId));
	            return "admin/reviews/reviewpost";
	        }

	        reviewService.saveReview(restaurantId, reviewForm, getCurrentUsername());
	        return "redirect:/admin/restaurants/" + restaurantId;
	    }

	    @GetMapping("/editReviewForm/{reviewId}")
	    public String editReviewForm(@PathVariable("reviewId") Integer reviewId, Model model) {
	    	if (!isPaidUser() && !isAdmin()) {
	            return "redirect:/user/paid"; // 有料会員ページにリダイレクト
	        }
	    	Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("Invalid review Id:" + reviewId));
	    	Restaurant restaurant = review.getRestaurant();
	    	
	    	// 評価やコメントの事前設定
	        ReviewForm reviewForm = new ReviewForm();
	        reviewForm.setScore(review.getScore());
	        reviewForm.setContent(review.getContent());


	        model.addAttribute("reviewForm", reviewForm);
	        model.addAttribute("review", review);
	        model.addAttribute("restaurant", restaurant);
	        return "admin/reviews/reviewedit";
	    }

	    @PostMapping("/updateReview/{reviewId}")
	    public String updateReview(@PathVariable("reviewId") Integer reviewId, @ModelAttribute @Validated ReviewForm reviewForm, BindingResult result, Model model) {
	    	if (!isPaidUser() && !isAdmin()) {
	            return "redirect:/user/paid"; // 有料会員ページにリダイレクト
	        }
	    	
	    	if (result.hasErrors()) {
	    		Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("Invalid review Id:" + reviewId));
	            model.addAttribute("review", review);
	            return "admin/reviews/reviewedit";
	        }

	        reviewService.updateReview(reviewId, reviewForm, getCurrentUsername());
	        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("Invalid review Id:" + reviewId));
	        return "redirect:/admin/restaurants/" + review.getRestaurant().getId();
	    }
	    
	    @PostMapping("/deleteReview/{reviewId}")
	    public String deleteReview(@PathVariable("reviewId") Integer reviewId, RedirectAttributes redirectAttributes) {
	    	if (!isAdmin()) {
	            return "redirect:/user/paid"; // 有料会員ページにリダイレクト
	        }
	    	
	    	Optional<Review> optionalReview = reviewRepository.findById(reviewId);
	    	    
	    	    if(optionalReview.isPresent()) {
	    	        Review review = optionalReview.get();
	    	        reviewRepository.deleteById(reviewId);
	    	        
	    	        redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。");
	    	        return "redirect:/admin/restaurants/" + review.getRestaurant().getId();
	    	    } else {
	    	        redirectAttributes.addFlashAttribute("errorMessage", "レビューが見つかりませんでした。");
	    	        return "redirect:/admin/restaurants";
	    	    }
	    }

	    @GetMapping("/reviewList/{restaurantId}")
	    public String reviewList(@PathVariable("restaurantId") Integer restaurantId, Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.DESC) Pageable pageable) {
	        Page<Review> reviewPage = reviewRepository.findByRestaurantIdOrderByScoreDesc(restaurantId, pageable);
	        Restaurant restaurant = restaurantService.findById(restaurantId);

	        model.addAttribute("reviewPage", reviewPage);
	        model.addAttribute("restaurant", restaurant);

	        return "admin/reviews/review";
	    }
}


