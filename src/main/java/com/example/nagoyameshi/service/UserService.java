package com.example.nagoyameshi.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.UserNotFoundException;
import com.example.nagoyameshi.entity.Role;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.entity.VerificationToken;
import com.example.nagoyameshi.form.SignupForm;
import com.example.nagoyameshi.form.UserEditForm;
import com.example.nagoyameshi.repository.RoleRepository;
import com.example.nagoyameshi.repository.UserRepository;
import com.example.nagoyameshi.repository.VerificationTokenRepository;

@Service
public class UserService {
	
	 private final UserRepository userRepository;
     private final RoleRepository roleRepository;
     private final PasswordEncoder passwordEncoder;
     private final VerificationTokenRepository verificationTokenRepository;
     
     public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, VerificationTokenRepository verificationTokenRepository) {
         this.userRepository = userRepository;
         this.roleRepository = roleRepository;        
         this.passwordEncoder = passwordEncoder;
         this.verificationTokenRepository = verificationTokenRepository;
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

         return userRepository.save(user);
       
     }  
     
     @Transactional
     public void update(UserEditForm userEditForm) {
         User user = userRepository.getReferenceById(userEditForm.getId());
         
         user.setName(userEditForm.getName());
         user.setFurigana(userEditForm.getFurigana());
         user.setPostalCode(userEditForm.getPostalCode());
         user.setAddress(userEditForm.getAddress());
         user.setPhoneNumber(userEditForm.getPhoneNumber());
         user.setEmail(userEditForm.getEmail());      
         
         userRepository.save(user);
     }
     
     // メールアドレスが登録済みかどうかをチェックする
     public boolean isEmailRegistered(String email) {
         return userRepository.findByEmail(email).isPresent();  
     }
     
     // パスワードとパスワード（確認用）の入力値が一致するかどうかをチェックする
     public boolean isSamePassword(String password, String passwordConfirmation) {
         return password.equals(passwordConfirmation);
     }
     
  // ユーザーを有効にする
     @Transactional
     public void enableUser(User user) {
         user.setEnabled(true); 
         userRepository.save(user);
     }
     
     // メールアドレスが変更されたかどうかをチェックする
     public boolean isEmailChanged(UserEditForm userEditForm) {
         User currentUser = userRepository.getReferenceById(userEditForm.getId());
         return !userEditForm.getEmail().equals(currentUser.getEmail());      
     }
     
     public User getCurrentUser() {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         if (authentication == null || !authentication.isAuthenticated()) {
             return null;
         }

         Object principal = authentication.getPrincipal();
         if (principal instanceof UserDetails) {
             String username = ((UserDetails) principal).getUsername();
             return userRepository.findByEmail(username)
             		.orElseThrow(() -> new UserNotFoundException("User not found"));
         } else {
             return null;
         }
     }
     
     @Transactional
     public void upgrade(Integer id) {
         User user = userRepository.findById(id)
             .orElseThrow(() -> new UserNotFoundException("User not found"));
         user.setPaid(true);
         userRepository.save(user);
     }

     @Transactional
     public void downgrade(Integer id) {
         User user = userRepository.findById(id)
             .orElseThrow(() -> new UserNotFoundException("User not found"));
         user.setPaid(false);
         userRepository.save(user);
     }

     @Transactional
     public void cancel(Integer id) {
         User user = userRepository.findById(id)
             .orElseThrow(() -> new UserNotFoundException("User not found"));
      // VerificationTokenの削除
         VerificationToken token = verificationTokenRepository.findByUser(user);
         if (token != null) {
             verificationTokenRepository.delete(token);
         }
         
         userRepository.delete(user);
     }
     
     @Transactional
     public void upgradeUserToPaid(String email) {
    	 User user = userRepository.findByEmail(email)
    	            .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    	     user.setPaid(true);
    	     userRepository.save(user);
  }

     @Transactional
     public void downgradeUserToFree(String email) {
         User user = userRepository.findByEmail(email)
             .orElseThrow(() -> new UserNotFoundException("User not found"));
         user.setPaid(false);
         userRepository.save(user);
     }
     
  // ユーザーをデータベースに保存するメソッド
     @Transactional
     public User save(User user) {
    	 return userRepository.save(user);
     }
     
  // メールでユーザーを探すメソッド
     @Transactional
	public User findByEmail(String email) {
    	 User user = userRepository.findByEmail(email)
		           .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
		    return user;
	}
}
