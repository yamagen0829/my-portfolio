package com.example.nagoyameshi.controller;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReviewForm;
import com.example.nagoyameshi.repository.ReviewRepository;
import com.example.nagoyameshi.repository.UserRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.RestaurantService;
import com.example.nagoyameshi.service.ReviewService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;
    private final RestaurantService restaurantService;
    private final UserRepository userRepository; 
    
    public ReviewController(ReviewService reviewService, ReviewRepository reviewRepository, RestaurantService restaurantService, UserRepository userRepository) {
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
    
    private boolean isPaidUser() {
        User user = getCurrentUser();
        return user != null && Boolean.TRUE.equals(user.getPaid());
    }

    @GetMapping("/postReviewForm/{restaurantId}")
    public String postReviewForm(@PathVariable("restaurantId") Integer restaurantId,
    		                     @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
    		                     Model model) {
    	 // ログインしているユーザー情報を取得
        User currentUser = getCurrentUser();
        boolean isPaidUser = false;
        
        if (currentUser != null) {
            isPaidUser = currentUser.getPaid() != null? currentUser.getPaid() : false;
        }
//    	User currentUser = userDetailsImpl.getUser();
//    	if (currentUser.getPaid() == null || !currentUser.getPaid()) {
//            return "redirect:/user/paid"; // 有料会員ページにリダイレクト
//        }
    	
    	model.addAttribute("reviewForm", new ReviewForm());
        model.addAttribute("restaurant", restaurantService.findById(restaurantId));
        model.addAttribute("isPaidUser", isPaidUser);
        return "reviews/reviewpost";  
    }

    @PostMapping("/submitReview/{restaurantId}")
    public String submitReview(@PathVariable("restaurantId") Integer restaurantId,
    		                   @RequestParam("redirectUrl") String redirectUrl,
    		                   @ModelAttribute @Valid ReviewForm reviewForm, 
    		                   BindingResult result, 
    		                   Model model) {
    	if (!isPaidUser()) {
            return "redirect:/user/paid"; // 有料会員ページにリダイレクト
        }
    	
    	if (result.hasErrors()) {
            model.addAttribute("restaurant", restaurantService.findById(restaurantId));
            model.addAttribute("redirectUrl", redirectUrl);
            return "reviews/reviewpost";
        }
    	// カンマが含まれていた場合の対策 (予備的対策)
        redirectUrl = redirectUrl.replaceAll(",$", "");

        reviewService.saveReview(restaurantId, reviewForm, getCurrentUsername());
      
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/editReviewForm/{reviewId}")
    public String editReviewForm(@PathVariable("reviewId") Integer reviewId, Model model) {
    	if (!isPaidUser()) {
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
        return "reviews/reviewedit";
    }

    @PostMapping("/updateReview/{reviewId}")
    public String updateReview(@PathVariable("reviewId") Integer reviewId,
    		                   @RequestParam("redirectUrl") String redirectUrl,
    		                   @ModelAttribute @Valid ReviewForm reviewForm, 
    		                   BindingResult result, 
    		                   Model model) {
    	if (!isPaidUser()) {
            return "redirect:/user/paid"; // 有料会員ページにリダイレクト
        }
    	
    	if (result.hasErrors()) {
    		Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("Invalid review Id:" + reviewId));
            model.addAttribute("review", review);
            model.addAttribute("redirectUrl", redirectUrl);
            return "reviews/reviewedit";
        }
    	
    	// カンマが含まれていた場合の対策 (予備的対策)
        redirectUrl = redirectUrl.replaceAll(",$", "");

        reviewService.updateReview(reviewId, reviewForm, getCurrentUsername());
       
        return "redirect:" + redirectUrl;
    }
    
    @PostMapping("/deleteReview/{reviewId}")
    public String deleteReview(@PathVariable("reviewId") Integer reviewId, 
                               @RequestParam("redirectUrl") String redirectUrl,
                               RedirectAttributes redirectAttributes) {
    	 Optional<Review> optionalReview = reviewRepository.findById(reviewId);
    	    
    	    if(optionalReview.isPresent()) {
    	        Review review = optionalReview.get();
    	        reviewRepository.deleteById(reviewId);
    	        
    	        redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。");
    	        return "redirect:" + redirectUrl;
    	    } else {
    	        redirectAttributes.addFlashAttribute("errorMessage", "レビューが見つかりませんでした。");
    	        return "redirect:/restaurants";
    	    }
    }

    @GetMapping("/reviewList/{restaurantId}")
    public String reviewList(@PathVariable("restaurantId") Integer restaurantId, Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.DESC) Pageable pageable) {
        Page<Review> reviewPage = reviewRepository.findByRestaurantIdOrderByScoreDesc(restaurantId, pageable);
        Restaurant restaurant = restaurantService.findById(restaurantId);
        
        // ログインしているユーザー情報を取得
        User currentUser = getCurrentUser();
        boolean isPaidUser = false;
        
        if (currentUser != null) {
            isPaidUser = currentUser.getPaid() != null? currentUser.getPaid() : false;
        }

        model.addAttribute("reviewPage", reviewPage);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("isPaidUser", isPaidUser);

        return "reviews/review";
    }
}
