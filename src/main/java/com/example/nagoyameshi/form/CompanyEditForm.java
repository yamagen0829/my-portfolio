package com.example.nagoyameshi.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanyEditForm {
	@NotNull
	private Integer id;
	
	@NotBlank(message = "会社名を入力してください。")
	private String companyName;
	
	@NotBlank(message = "代表者名を入力してください。")
	private String name;
    
	@NotBlank(message = "郵便番号を入力してください。")
	private String postalCode;
	
	@NotBlank(message = "住所を入力してください。")
	private String address;
	
	@NotBlank(message = "事業内容を入力してください。")
	private String businessContent;
} 
