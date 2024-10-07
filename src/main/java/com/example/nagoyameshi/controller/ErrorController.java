package com.example.nagoyameshi.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class ErrorController {

	@GetMapping("/errorPage")
    public String errorPage(Model model) {
        return "errorPage"; // エラー表示用のThymeleafテンプレート
    }
}
