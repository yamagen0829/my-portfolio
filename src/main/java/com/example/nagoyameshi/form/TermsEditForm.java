package com.example.nagoyameshi.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TermsEditForm {
	@NotNull
	private Integer id;
	
	@NotBlank(message = "入力してください。")
	private String article1;
	
	@NotBlank(message = "入力してください。")
	private String article2;
    
	@NotBlank(message = "入力してください。")
	private String article3;
}
