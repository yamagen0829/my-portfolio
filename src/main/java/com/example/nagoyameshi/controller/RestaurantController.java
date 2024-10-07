package com.example.nagoyameshi.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReservationInputForm;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.repository.ReviewRepository;
import com.example.nagoyameshi.repository.UserRepository;
import com.example.nagoyameshi.service.FavoriteService;
import com.example.nagoyameshi.service.RestaurantService;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {
	private final RestaurantRepository restaurantRepository;
	private final RestaurantService restaurantService;
	private final ReviewRepository reviewRepository;
//	private final FavoriteRepository favoriteRepository;
	private final FavoriteService favoriteService;
//	private final UserService userService;
	private final UserRepository userRepository;
    
    public RestaurantController(RestaurantRepository restaurantRepository, RestaurantService restaurantService, ReviewRepository reviewRepository, FavoriteService favoriteService, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantService = restaurantService;  
        this.reviewRepository = reviewRepository;
//        this.favoriteRepository = favoriteRepository;
        this.favoriteService = favoriteService;
//        this.userService = userService;
        this.userRepository = userRepository;
    }     
  
    @GetMapping
    public String index(@RequestParam(name = "keyword", required = false) String keyword,
                        @RequestParam(name = "address", required = false) String address,
                        @RequestParam(name = "price", required = false) Integer price, 
                        @RequestParam(name = "category", required = false) String category,
                        @RequestParam(name = "order", required = false) String order,
                        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
                        Model model) 
    {
        Page<Restaurant> restaurantPage;
                
        if (keyword != null && !keyword.isEmpty()) {
        	if (order != null && order.equals("priceAsc")) {
                restaurantPage = restaurantRepository.findByNameLikeOrAddressLikeOrderByPriceAsc("%" + keyword + "%", "%" + keyword + "%", pageable);
            } else if (order != null && order.equals("scoreDesc")) {
                restaurantPage = restaurantRepository.findByNameOrAddressOrderByAverageScoreDesc("%" + keyword + "%", "%" + keyword + "%", pageable);
            } else {
                restaurantPage = restaurantRepository.findByNameLikeOrAddressLikeOrderByCreatedAtDesc("%" + keyword + "%", "%" + keyword + "%", pageable);
            } 
        } else if (address != null && !address.isEmpty()) {
        	if (order != null && order.equals("priceAsc")) {
                restaurantPage = restaurantRepository.findByAddressLikeOrderByPriceAsc("%" + address + "%", pageable);
            } else if (order != null && order.equals("scoreDesc")) {
                restaurantPage = restaurantRepository.findByAddressOrderByAverageScoreDesc("%" + address + "%", pageable); 
            } else {
                restaurantPage = restaurantRepository.findByAddressLikeOrderByCreatedAtDesc("%" + address + "%", pageable);
            }  
        } else if (price != null) {
        	if (order != null && order.equals("priceAsc")) {
                restaurantPage = restaurantRepository.findByPriceLessThanEqualOrderByPriceAsc(price, pageable);
            } else if (order != null && order.equals("scoreDesc")) {
                restaurantPage = restaurantRepository.findByPriceOrderByAverageScoreDesc(price, pageable); 
            } else {
                restaurantPage = restaurantRepository.findByPriceLessThanEqualOrderByCreatedAtDesc(price, pageable);
            }
        } else if (category != null && !category.isEmpty()) {
        	if(order != null && order.equals("scoreDesc")) {
        	    restaurantPage = restaurantRepository.findByCategoryNameOrderByAverageScoreDesc(category, pageable);
        	} else {
        		restaurantPage = restaurantRepository.findByCategoryNameContaining(category, pageable);
        	}
        } else { 
        	if (order != null && order.equals("priceAsc")) {
                restaurantPage = restaurantRepository.findAllByOrderByPriceAsc(pageable);
            } else if (order != null && order.equals("scoreDesc")) {
                restaurantPage = restaurantRepository.findAllByOrderByAverageScoreDesc(pageable); 
            } else {
                restaurantPage = restaurantRepository.findAllByOrderByCreatedAtDesc(pageable);   
            }
        }                
        
        model.addAttribute("restaurantPage", restaurantPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("address", address);
        model.addAttribute("price", price);
        model.addAttribute("category", category);
        model.addAttribute("order", order);
        
        return "restaurants/index";
    }
      
    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Integer id, Model model) {
        Restaurant restaurant = restaurantService.findById(id);
        List<Review> reviews = reviewRepository.findByRestaurantId(id);
        
     // ログインしているユーザー情報を取得
        User currentUser = getCurrentUser();
        boolean isFavorite = false;
        boolean isPaidUser = false;
        
        if (currentUser != null) {
            isFavorite = favoriteService.isFavorite(currentUser.getId(), id);
            isPaidUser = currentUser.getPaid() != null? currentUser.getPaid() : false;
        }
        
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("reservationInputForm", new ReservationInputForm());
        model.addAttribute("reviews", reviews);
        model.addAttribute("isFavorite", isFavorite);
        model.addAttribute("isPaidUser", isPaidUser);
        
        return "restaurants/show";
    } 
    
// // 現在のユーザーが有料会員かどうかを確認するメソッド
//    private String getCurrentUsername() {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (principal instanceof UserDetails) {
//            return ((UserDetails) principal).getUsername();
//        } else {
//            return principal.toString();
//        }
//    }
//    
//    private User getCurrentUser() {
//        String email = getCurrentUsername();
//        Optional<User> optionalUser = userRepository.findByEmail(email);
//        return optionalUser.orElse(null);
//    }
//    
//    private boolean isPaidUser() {
//        User user = getCurrentUser();
//        return user != null && Boolean.TRUE.equals(user.getPaid());
//    }
    
    @PostMapping("/{id}/addFavorite")
    public String addFavorite(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
    	User currentUser = getCurrentUser();
    	
    	if (currentUser == null || !Boolean.TRUE.equals(currentUser.getPaid())) {
            return "redirect:/user/paid"; // 有料会員ページにリダイレクト
        }
    	
    	try {    
	            favoriteService.addFavorite(id, currentUser.getId());
	            redirectAttributes.addFlashAttribute("message", "お気に入りに追加しました。");
	        } catch (Exception e) {	
	        	e.printStackTrace();	
	        	redirectAttributes.addFlashAttribute("errorMessage", "お気に入りの追加中にエラーが発生しました: " + e.getMessage());	
	        }
	
	        return "redirect:/restaurants/" + id;
    }

    @PostMapping("/{id}/removeFavorite")
    public String removeFavorite(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
    	User currentUser = getCurrentUser();
    	if (currentUser == null || !Boolean.TRUE.equals(currentUser.getPaid())) {
            return "redirect:/user/paid"; // 有料会員ページにリダイレクト
        }
    
        try {
            favoriteService.removeFavorite(id, currentUser.getId());
            redirectAttributes.addFlashAttribute("message", "お気に入りを解除しました。");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "お気に入りの解除中にエラーが発生しました: " + e.getMessage());
        }

        return "redirect:/restaurants/" + id;
    }
    
    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            Optional<User> optionalUser = userRepository.findByEmail(email);
            return optionalUser.orElse(null);
        }
        return null;
    }
//    	if (!isPaidUser()) {
//            return "redirect:/user/paid"; // 有料会員ページにリダイレクト
//        }
//    	
//    	try {
//    	    User currentUser = userService.getCurrentUser();
//	        if (currentUser != null) {
//	            favoriteService.removeFavorite(id, currentUser.getId());
//	
//	            redirectAttributes.addFlashAttribute("message", "お気に入りを解除しました。");
//	          } else {
//	        	  redirectAttributes.addFlashAttribute("errorMessage", "ユーザーが認識されませんでした。");
//	          }
//	        } catch (Exception e) {	
//	        	e.printStackTrace();	
//	        	redirectAttributes.addFlashAttribute("errorMessage", "お気に入りの解除中にエラーが発生しました: " + e.getMessage());
//	        }
//	        return "redirect:/restaurants/" + id;
//    }
}
