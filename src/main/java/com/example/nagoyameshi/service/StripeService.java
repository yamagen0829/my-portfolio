package com.example.nagoyameshi.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.User;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.PaymentMethod;
import com.stripe.model.SetupIntent;
import com.stripe.model.StripeObject;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.SetupIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class StripeService {
	@Value("${stripe.api-key}")
	private String stripeApiKey;
	
	@Value("${stripe.price-id}")	
	private String stripePriceId;	
    
	private final UserService userService;
	
	//private static final Logger logger = LoggerFactory.getLogger(StripeService.class);
	
    
    public StripeService(UserService userService) {
        this.userService = userService;
    }   
	
    public String createStripeSession(String userEmail, HttpServletRequest httpServletRequest) throws StripeException {
        Stripe.apiKey = stripeApiKey;
        
     // ログを追加
        System.out.println("Creating Stripe session for user: " + userEmail);
        
        String requestUrl = httpServletRequest.getRequestURL().toString();
        String host = requestUrl.replace(httpServletRequest.getRequestURI(), "");
        
      //定期料金オブジェクトの定義
      	SessionCreateParams.LineItem.PriceData.Recurring recurring = SessionCreateParams.LineItem.PriceData.Recurring
      		.builder()
      		.setInterval(SessionCreateParams.LineItem.PriceData.Recurring.Interval.MONTH).build();
      	
      //価格データを価格の繰り返し情報で設定
      	SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
      		     .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
      						.setName("NAGOYAMESHI有料会員").build())
      		     .setUnitAmount(300L)
      			 .setCurrency("jpy")
      			 .setRecurring(recurring) //ここで定期的な料金設定
      			 .build();
      		
        SessionCreateParams params =
            SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setPrice(stripePriceId)
                        .setQuantity(1L)
                        .build())
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSuccessUrl(host + "/user/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(host + "/user/cancel")
                .setCustomerEmail(userEmail)
                .build();
        
            Session session = Session.create(params);
            
         // セッションIDの確認
            System.out.println("Created Stripe session: " + session.getId());
            
            return session.getId();
    } 
    
 // Billingポータル用セッションを生成するメソッド
    public String createBillingPortalSession(String customerId, String returnUrl) throws StripeException {
        Stripe.apiKey = stripeApiKey;
        
//     // ログを追加
//        logger.info("Creating Billing Portal session for customer: {}" + customerId);
//        
        com.stripe.param.billingportal.SessionCreateParams params =
    		com.stripe.param.billingportal.SessionCreateParams.builder()
    			.setReturnUrl(returnUrl)
    			.setCustomer(customerId)
    			.build();
        com.stripe.model.billingportal.Session session = com.stripe.model.billingportal.Session.create(params);
        
//     // セッションURLの確認
//        logger.info("Created Billing Portal session URL: {}" + session.getUrl());
//        
          return session.getUrl();  // BillingポータルのURL取得
    }

    
    public String createCustomer(String email) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        CustomerCreateParams params = CustomerCreateParams.builder()
            .setEmail(email)
            .build();

        Customer customer = Customer.create(params);
        return customer.getId();
    }
    
 // セッションからユーザー情報を取得し、UserServiceクラスを介してデータベースに登録する 
    public void processSessionCompleted(Event event) {
    	Optional<StripeObject> optionalStripeObject = event.getDataObjectDeserializer().getObject();
    	if (optionalStripeObject.isPresent()) {
            Session session = (Session) optionalStripeObject.get();
            String customerId = session.getCustomer();
            User user = userService.findByEmail(session.getCustomerDetails().getEmail());
            userService.upgradeUserToPaid(session.getCustomerDetails().getEmail());
            
            if (user != null && customerId != null) {
                user.setStripeCustomerId(customerId);
                userService.save(user); //ここでユーザーを保存し、stripeCustomerIdを更新
//                logger.info("Updated user with stripe customer id: " + customerId);
            }// else {
//                logger.error("User or customer ID not found");
           // }
        } //else {
//            logger.error("Failed to deserialize Stripe session");
        //}
    }
    
    public void processPaymentFailed(Event event) {		
	}
    
    public void cancelSubscription(String subscriptionId) throws StripeException {
        Stripe.apiKey = stripeApiKey;
        Subscription subscription = Subscription.retrieve(subscriptionId);
        subscription.cancel();
    }
    
    public String createSetupIntent(String customerId) throws StripeException {
        Stripe.apiKey = stripeApiKey;
        
        SetupIntentCreateParams params = SetupIntentCreateParams.builder()
                .setCustomer(customerId)
                .addPaymentMethodType("card")
                .build();
        
        SetupIntent setupIntent = SetupIntent.create(params);
        return setupIntent.getClientSecret();
    }
    
    public void updateDefaultPaymentMethod(String customerId, String PaymentMethodId) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        Map<String, Object> invoiceSettings = new HashMap<>();
        invoiceSettings.put("default_payment_method", PaymentMethodId);

        Map<String, Object> params = new HashMap<>();
        params.put("invoice_settings", invoiceSettings);

        Customer customer = Customer.retrieve(customerId);
        customer.update(params);
    }
    
    public void detachPaymentMethod(String paymentMethodId) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);
        paymentMethod.detach();
    }

    public void processSubscriptionCreated(Event event) {
	    Optional<StripeObject> optionalStripeObject = event.getDataObjectDeserializer().getObject();
	    if (optionalStripeObject.isPresent()) {
	        Subscription subscription = (Subscription) optionalStripeObject.get();
	        String customerId = subscription.getCustomer();
	        // 必要に応じて他の処理を実装する
	     //   logger.info("Subscription created for customer id: " + customerId);
	    } //else {
	       // logger.error("Failed to deserialize Stripe subscription event");
	   // }
    } 	    
}