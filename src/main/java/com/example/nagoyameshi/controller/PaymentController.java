package com.example.nagoyameshi.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.CustomerService;
import com.example.nagoyameshi.service.StripeService;
import com.example.nagoyameshi.service.UserService;
import com.stripe.exception.StripeException;

@RestController
@RequestMapping("/api")
public class PaymentController {
	
	@Autowired
    private StripeService stripeService;
	
    @Autowired
    private UserService userService;
    
    @Autowired
    private CustomerService customerService;
    
    @GetMapping("/getPaymentMethodId")
    public ResponseEntity<Map<String, String>> getPaymentMethodId(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String paymentMethodId = customerService.getPaymentMethodId(userDetails.getUser().getId());
       
        if(paymentMethodId == null) {
            return ResponseEntity.status(400).body(Map.of("error", "Payment Method ID not provided"));
        }
        
        Map<String, String> response = Map.of("paymentMethodId", paymentMethodId);
        return ResponseEntity.ok(response);
    }
    
    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/createSetupIntent")
    public ResponseEntity<?> createSetupIntent(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String customerId = customerService.getStripeCustomerId(userDetails.getUser().getEmail()); // 顧客IDを取得
        
        if(customerId == null) {
            return ResponseEntity.status(400).body(Map.of("error", "Customer ID not found"));
        }
        
        try {
            String clientSecret = stripeService.createSetupIntent(customerId);
            return ResponseEntity.ok(Map.of("clientSecret", clientSecret));
        } catch (StripeException e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    
    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/updatePaymentMethod")
    public ResponseEntity<?> updatePaymentMethod(@RequestBody Map<String, String> payload, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    	if (userDetails == null || userDetails.getUser() == null) {
            return ResponseEntity.status(401).body(Map.of("success", false, "error", "User not authenticated"));
        }
    	
    	String paymentMethodId = payload.get("paymentMethodId");
    	
    	if(paymentMethodId == null) {
            return ResponseEntity.status(400).body(Map.of("error", "Payment Method ID not provided"));
        }
    	
        String customerId = customerService.getStripeCustomerId(userDetails.getUser().getEmail()); // 実際の顧客IDを取得するロジックを追加
        
        if(customerId == null) {
            return ResponseEntity.status(400).body(Map.of("error", "Customer ID not found"));
        }

        try {
            stripeService.updateDefaultPaymentMethod(customerId, paymentMethodId);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (StripeException e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "error", e.getMessage()));
        }
    }

	@CrossOrigin(origins = "http://localhost:8080") 
    @PostMapping("/deletePaymentMethod")
    public ResponseEntity<?> deletePaymentMethod(@RequestBody Map<String, String> payload, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		if (userDetails == null || userDetails.getUser() == null) {
            return ResponseEntity.status(401).body(Map.of("success", false, "error", "User not authenticated"));
        }
		
		String paymentMethodId = payload.get("paymentMethodId");
		
		if(paymentMethodId == null) {
	        return ResponseEntity.status(400).body(Map.of("error", "Payment Method ID not provided"));
	    }
		
        String customerId = customerService.getStripeCustomerId(userDetails.getUser().getEmail());
        
        if(customerId == null) {
            return ResponseEntity.status(400).body(Map.of("error", "Customer ID not found"));
        }
        
        try {
            stripeService.detachPaymentMethod(paymentMethodId);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (StripeException e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "error", e.getMessage()));
        }
    }
}
