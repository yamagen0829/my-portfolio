package com.example.nagoyameshi.form;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservationInputForm {
	@NotBlank(message = "予約日を選択してください。")
    private String fromReservationDate; 
	
	@NotBlank(message = "予約時間を入力してください。")
    private String fromReservationTime;
    
    @NotNull(message = "人数を入力してください。")
    @Min(value = 1, message = "人数は1人以上に設定してください。")
    private Integer numberOfPeople; 

    // 予約日を取得する
    public LocalDate getReservationDate() {
    	try {
            return LocalDate.parse(getFromReservationDate(), DateTimeFormatter.ISO_DATE);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("予約日が無効です。");
        }
    }
    
   // 予約時間を取得する
    public LocalTime getReservationTime() {
    	try {
            return LocalTime.parse(getFromReservationTime(), DateTimeFormatter.ISO_TIME);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("予約時間が無効です。");
        }
    }	
}
