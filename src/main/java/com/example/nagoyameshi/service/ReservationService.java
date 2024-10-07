package com.example.nagoyameshi.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReservationRegisterForm;
import com.example.nagoyameshi.repository.ReservationRepository;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.repository.UserRepository;

@Service
public class ReservationService {
	private final ReservationRepository reservationRepository;  
    private final RestaurantRepository restaurantRepository;  
    private final UserRepository userRepository;
    private final EmailService emailService;
    
    @Autowired
    public ReservationService(ReservationRepository reservationRepository, RestaurantRepository restaurantRepository, UserRepository userRepository, EmailService emailService) {
        this.reservationRepository = reservationRepository;  
        this.restaurantRepository = restaurantRepository;  
        this.userRepository = userRepository;
        this.emailService = emailService;
    }    
    
    @Transactional
    public void create(ReservationRegisterForm reservationRegisterForm) { 
        Reservation reservation = new Reservation();
        
        Restaurant restaurant = restaurantRepository.getReferenceById(reservationRegisterForm.getRestaurantId());
        User user = userRepository.getReferenceById(reservationRegisterForm.getUserId());
        LocalDate reservationDate = LocalDate.parse(reservationRegisterForm.getReservationDate());
        LocalTime reservationTime = LocalTime.parse(reservationRegisterForm.getReservationTime());
                
        reservation.setRestaurant(restaurant);
        reservation.setUser(user);
        reservation.setReservationDate(reservationDate);
        reservation.setReservationTime(reservationTime);
        reservation.setNumberOfPeople(reservationRegisterForm.getNumberOfPeople());
        reservation.setAmount(reservationRegisterForm.getAmount());
        
        reservationRepository.save(reservation);
        
        String subject = "予約完了のお知らせ";
        String text = "ご予約が完了しました。以下の内容でご確認ください：" +
                      "\nレストラン：" + restaurant.getName() +
                      "\n予約日：" + reservationDate +
                      "\n予約時間：" + reservationTime +
                      "\n人数：" + reservationRegisterForm.getNumberOfPeople();
        
        emailService.sendReservationConfirmation(user.getEmail(), subject, text);
    }    
    
  // 人数が定員以下かどうかをチェックする
	public boolean isWithinNumberOfSeat(Integer numberOfPeople, Integer numberOfSeat) {
		return numberOfPeople <= numberOfSeat;
	}

  // 料金を計算する
	public Integer calculateAmount(LocalDate reservationDate, LocalTime reservationTime, Integer price, Integer numberOfPeople) {
		int amount = price * numberOfPeople;
		return amount;
	}
	
	public LocalTime parseTimeWith24HourSupport(String time) {
        if ("24:00".equals(time)) {
        	return LocalTime.MIDNIGHT.plusHours(24);
        }
        	 try {
                 return LocalTime.parse(time, DateTimeFormatter.ofPattern("H:mm"));
             } catch (Exception e) {
                 return LocalTime.parse(time);
             }   	 
    }
	
	// 営業時間チェック
    public boolean isWithinBusinessHours(LocalTime reservationTime, String openingHours) {
        String[] hours = openingHours.split("~");
        LocalTime openingTime = parseTimeWith24HourSupport(hours[0].trim());
        LocalTime closingTime = parseTimeWith24HourSupport(hours[1].trim());
        
        boolean withinHours;	
        
        if (closingTime.equals(LocalTime.MIDNIGHT.plusHours(24)) || closingTime.isBefore(openingTime)) {	
        return !reservationTime.isBefore(openingTime) || !reservationTime.isAfter(closingTime.minusHours(24));	
        } else {	
        return !reservationTime.isBefore(openingTime) && !reservationTime.isAfter(closingTime);
        }
    }

    // 現在の時間より後かどうかをチェックする
    public boolean isAfterCurrentTime(LocalDate reservationDate, LocalTime reservationTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationDateTime = LocalDateTime.of(reservationDate, reservationTime);

        boolean isAfter = reservationDateTime.isAfter(now);
        return isAfter;
    }

}
