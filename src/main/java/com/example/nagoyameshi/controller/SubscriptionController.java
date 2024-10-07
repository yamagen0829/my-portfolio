package com.example.nagoyameshi.controller;

import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.service.StripeService;
import com.example.nagoyameshi.service.UserService;
import com.stripe.exception.StripeException;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SubscriptionController {

    private final StripeService stripeService;
    private final UserService userService;

    public SubscriptionController(StripeService stripeService, UserService userService) {
        this.stripeService = stripeService;
        this.userService = userService;
    }

    @GetMapping("/user/success")
    public String success(@RequestParam("session_id") String sessionId, RedirectAttributes redirectAttributes) {
        // 成功した場合の処理
        redirectAttributes.addFlashAttribute("successMessage", "サブスクリプション支払いが成功しました。");
        return "redirect:/user"; // 成功メッセージページにリダイレクト
    }

    @GetMapping("/user/cancel")
    public String cancel(RedirectAttributes redirectAttributes) {
        // キャンセルした場合の処理
        redirectAttributes.addFlashAttribute("errorMessage", "サブスクリプション支払いをキャンセルしました。");
        return "redirect:/user"; // キャンセルページにリダイレクト
    }

    @GetMapping("/user/checkout")
    public String checkoutPage() {
        // チェックアウトページの表示
        return "user/checkout";
    }

    @PostMapping("/user/checkout")
    @ResponseBody
    public Map<String, Object> checkout(@RequestBody Map<String, String> payload, HttpServletRequest httpServletRequest) {
        String userEmail = payload.get("email");
        
        try {
        String sessionId = stripeService.createStripeSession(userEmail, httpServletRequest);

            return Map.of("sessionId", sessionId);
        } catch (StripeException e) {
        	e.printStackTrace();
        return Map.of("error", e.getMessage());
      }
    }
    
    @PostMapping("/user/cancel-subscription")
    @ResponseBody
    public Map<String, Object> cancelSubscription(@RequestBody Map<String, String> payload, @AuthenticationPrincipal User user) {
        String subscriptionId = payload.get("subscriptionId");

        try {
            stripeService.cancelSubscription(subscriptionId);
            return Map.of("success", true);
        } catch (StripeException e) {
            return Map.of("success", false, "error", e.getMessage());
        }
    }
}
