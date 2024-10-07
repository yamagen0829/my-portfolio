package com.example.nagoyameshi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.nagoyameshi.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;

@Controller
public class StripeWebhookController {
	private static final Logger logger = LoggerFactory.getLogger(StripeWebhookController.class);
	
	private final StripeService stripeService;
	 
    @Value("${stripe.api-key}")
    private String stripeApiKey;

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    public StripeWebhookController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/stripe/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        Stripe.apiKey = stripeApiKey;
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook signature verification failed.");
        }
        
        switch (event.getType()) {
	    case "checkout.session.completed":
	       // セッションが完了したときの処理
	       stripeService.processSessionCompleted(event);
	       break;
	    case "customer.subscription.created":
	       stripeService.processSubscriptionCreated(event);
	       break;
	    case "invoice.payment_failed":
	       // 支払いが失敗した際の処理
	       stripeService.processPaymentFailed(event);
	       break;
	    default:
	       // その他のイベントに対する処理
	       System.out.println("Unhandled event type: " + event.getType());
	       break;
        
        }
    
        return new ResponseEntity<>("Success", HttpStatus.OK);
    
    }
    
   
}
