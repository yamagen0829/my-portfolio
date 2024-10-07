package com.example.nagoyameshi.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.UserNotFoundException;
import com.example.nagoyameshi.entity.Role;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.SignupForm;
import com.example.nagoyameshi.repository.RoleRepository;
import com.example.nagoyameshi.repository.UserRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;

@Service
public class CustomerService {
	@Value("${stripe.api-key}")
	private String stripeApiKey;
	
	//private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	 private final UserRepository userRepository;
     private final RoleRepository roleRepository;
     private final PasswordEncoder passwordEncoder;
     private final StripeService stripeService;
     
     public CustomerService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, StripeService stripeService) {
         this.userRepository = userRepository;
         this.roleRepository = roleRepository;        
         this.passwordEncoder = passwordEncoder;
         this.stripeService = stripeService;
     }     
	
	 @Transactional
     public User create(SignupForm signupForm) {
         User user = new User();
         Role role = roleRepository.findByName("ROLE_GENERAL");
         
         user.setName(signupForm.getName());
         user.setFurigana(signupForm.getFurigana());
         user.setPostalCode(signupForm.getPostalCode());
         user.setAddress(signupForm.getAddress());
         user.setPhoneNumber(signupForm.getPhoneNumber());
         user.setEmail(signupForm.getEmail());
         user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
         user.setRole(role);
         user.setEnabled(false);
         user.setPaid(false);
         
         try {
             // Stripe顧客IDの作成
             String stripeCustomerId = stripeService.createCustomer(user.getEmail());
             user.setStripeCustomerId(stripeCustomerId);
         } catch (StripeException e) {
             //logger.error("Failed to create Stripe customer", e);
             // 必要に応じて適切な処理を行う(例: カスタム例外をスロー)
             throw new RuntimeException("Failed to create Stripe customer: " + e.getMessage(), e);
         }

         return userRepository.save(user);
       
     }
	 
	 public String getStripeCustomerId(String email) {
    	 User user = userRepository.findByEmail(email)
    	            .orElseThrow(() -> {
    	         //       logger.error("User not found with email: {}", email);
    	                return new UserNotFoundException("User not found with email: " + email);
    	            });
    	    String customerId = user.getStripeCustomerId();
//    	    if (customerId == null) {
//    	        logger.error("Stripe Customer ID is null for user: {}", email);
//    	    } else {
//    	        logger.debug("Fetched Stripe Customer ID {} for user: {}", customerId, email);
//    	    }
    	    return customerId;
    	
     }
	 
	 public void setDefaultPaymentMethod(String customerId, String paymentMethodId) throws StripeException {
 	    Customer customer = Customer.retrieve(customerId);

 	    Map<String, Object> invoiceSettings = new HashMap<>();
 	    invoiceSettings.put("default_payment_method", paymentMethodId);

 	    Map<String, Object> params = new HashMap<>();
 	    params.put("invoice_settings", invoiceSettings);

 	    customer.update(params);
 	    
 	 //   logger.debug("Updated default payment method for customer ID {}: {}", customerId, paymentMethodId);
 	}

	public String getPaymentMethodId(Integer id) {
		return null;
	}
	 
	 public void updatePaymentMethod(String email, String paymentMethodId) throws StripeException {
		    String customerId = getStripeCustomerId(email);
		    if (customerId == null)
		        throw new UserNotFoundException("Stripe customer ID not found for email: " + email);

		    setDefaultPaymentMethod(customerId, paymentMethodId);
		}
	 
	 public void deletePaymentMethod(String email, String paymentMethodId) throws StripeException {
		    String customerId = getStripeCustomerId(email);
		    if (customerId == null)
		        throw new UserNotFoundException("Stripe customer ID not found for email: " + email);

		    stripeService.detachPaymentMethod(paymentMethodId);
		}
}
