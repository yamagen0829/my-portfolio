package com.example.nagoyameshi.form;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RestaurantRegisterForm {
	@NotBlank(message = "店舗名を入力してください。")
    private String name;
	
	private MultipartFile imageFile;
	
	@NotBlank(message = "カテゴリ名を入力してください。")
	private String categoryName;
 
    @NotBlank(message = "説明を入力してください。")
    private String description;
 
    @NotBlank(message = "営業時間を入力してください。")
    private String openingHours;
    
    @NotNull(message = "料金を入力してください。")
    @Min(value = 1, message = "料金は1円以上に設定してください。")
    private Integer price;
 
    @NotBlank(message = "郵便番号を入力してください。")
    private String postalCode;
 
    @NotBlank(message = "住所を入力してください。")
    private String address;
 
    @NotBlank(message = "電話番号を入力してください。")
    private String phoneNumber;
    
    @NotBlank(message = "休日を入力してください。")
    private String regularHoliday;
    
    @NotNull(message = "座席数を入力してください。")
    @Min(value = 1, message = "座席数は1人以上に設定してください。")
    private Integer numberOfSeat;
}
