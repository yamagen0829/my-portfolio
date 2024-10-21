package com.example.nagoyameshi.controller;

import java.time.LocalDate;
import java.time.LocalTime;
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
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReservationInputForm;
import com.example.nagoyameshi.form.ReservationRegisterForm;
import com.example.nagoyameshi.repository.ReservationRepository;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.repository.UserRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.ReservationService;
import com.example.nagoyameshi.service.RestaurantService;

@Controller
public class ReservationController {
	private final ReservationRepository reservationRepository;
	private final RestaurantRepository restaurantRepository;
	private final RestaurantService restaurantService;
	private final ReservationService reservationService;
	private final UserRepository userRepository;
	
    
    public ReservationController(ReservationRepository reservationRepository, RestaurantRepository restaurantRepository, RestaurantService restaurantService, ReservationService reservationService, UserRepository userRepository) {        
        this.reservationRepository = reservationRepository;
        this.restaurantRepository = restaurantRepository;
        this.restaurantService = restaurantService;
        this.reservationService = reservationService;
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

    @GetMapping("/reservations")
    public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, Model model) {
        User user = userDetailsImpl.getUser();
        Page<Reservation> reservationPage = reservationRepository.findByUserOrderByCreatedAtDesc(user, pageable);
        
        model.addAttribute("reservationPage", reservationPage);         
        
        return "reservations/index";
    }
    
    @GetMapping("/restaurants/{id}/reservations/input")
    public String input(@PathVariable(name = "id") Integer id,
                        @ModelAttribute @Validated ReservationInputForm reservationInputForm,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes,
                        Model model)
    {   
        Restaurant restaurant = restaurantService.findById(id);
        Integer numberOfPeople = reservationInputForm.getNumberOfPeople();   
        Integer numberOfSeat = restaurant.getNumberOfSeat();
        
        User currentUser = getCurrentUser();
        boolean isPaidUser = false;
        
        if (currentUser != null) {
            isPaidUser = currentUser.getPaid() != null? currentUser.getPaid() : false;
        }
        
        if (numberOfPeople != null) {
            if (!reservationService.isWithinNumberOfSeat(numberOfPeople, numberOfSeat)) {
                FieldError fieldError = new FieldError(bindingResult.getObjectName(), "numberOfPeople", "人数が定員を超えています。");
                bindingResult.addError(fieldError);                
            }            
        }         
        
        if (bindingResult.hasErrors()) {            
            model.addAttribute("restaurant", restaurant); 
            model.addAttribute("isPaidUser", isPaidUser);
            model.addAttribute("errorMessage", "予約内容に不備があります。"); 
            return "restaurants/show";
        }
        
        redirectAttributes.addFlashAttribute("reservationInputForm", reservationInputForm);           
        
        return "redirect:/restaurants/{id}/reservations/confirm";
    }
    
    @GetMapping("/restaurants/{id}/reservations/confirm")
    public String confirm(@PathVariable(name = "id") Integer id,
                          @ModelAttribute ReservationInputForm reservationInputForm,
                          @AuthenticationPrincipal UserDetailsImpl userDetailsImpl, 
                          Model model) 
    {        
        Restaurant restaurant = restaurantRepository.getReferenceById(id);
        User user = userDetailsImpl.getUser(); 
                
      //予約日を取得する
        LocalDate reservationDate = reservationInputForm.getReservationDate();
 
      //予約時間を取得する
        String fromReservationTimeStr = reservationInputForm.getFromReservationTime();
        LocalTime reservationTime = reservationService.parseTimeWith24HourSupport(fromReservationTimeStr);
        
      // 人数を取得する
        Integer numberOfPeople = reservationInputForm.getNumberOfPeople();

     // 営業時間を取得する
        String openingHours = restaurant.getOpeningHours();

     // 営業時間チェック
        if (!reservationService.isWithinBusinessHours(reservationTime, openingHours)) {
            model.addAttribute("errorMessage", "営業時間外の時間が選択されました。");
            model.addAttribute("restaurant", restaurant);
            return "restaurants/show";
        }

     // 現在の時間より後かどうかチェック
        if (!reservationService.isAfterCurrentTime(reservationDate, reservationTime)) {
            model.addAttribute("errorMessage", "予約時間は現在の時間より後でなければなりません。");
            model.addAttribute("restaurant", restaurant);
            return "restaurants/show";
        } 
     
     // 料金を計算する
        Integer price = restaurant.getPrice();        
        Integer amount = reservationService.calculateAmount(reservationDate, reservationTime, price, numberOfPeople);
        
        ReservationRegisterForm reservationRegisterForm = new ReservationRegisterForm(restaurant.getId(), user.getId(), reservationDate.toString(), reservationTime.toString(), reservationInputForm.getNumberOfPeople(), amount);
        
        model.addAttribute("restaurant", restaurant);  
        model.addAttribute("reservationRegisterForm", reservationRegisterForm); 
        
        return "reservations/confirm";
    }
    @PostMapping("/restaurants/{id}/reservations/create")
    public String create(@ModelAttribute ReservationRegisterForm reservationRegisterForm) {                
        reservationService.create(reservationRegisterForm);        
        
        return "redirect:/reservations?reserved";
    }
    
 // 予約削除メソッド
    @PostMapping("/reservations/{id}/delete")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        reservationRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "予約をキャンセルしました。");
        return "redirect:/reservations";
    }
}
