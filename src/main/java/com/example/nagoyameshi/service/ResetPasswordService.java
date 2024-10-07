package com.example.nagoyameshi.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.PasswordResetDto;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.entity.VerificationToken;
import com.example.nagoyameshi.repository.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class ResetPasswordService {
	
	@Value("${app.host}")
    private String appHost;
	
	@Autowired
	private UserRepository userRepository;
	    
	@Autowired
	private VerificationTokenService verificationTokenService;
	    
	@Autowired
	private JavaMailSender mailSender;
	    
    @Autowired
	private PasswordEncoder passwordEncoder;
	        
	    @Transactional
	    public void sendResetToken(String email) {
	        Optional<User> optionalUser = userRepository.findByEmail(email);
	        optionalUser.ifPresent(user -> {
	            String token = UUID.randomUUID().toString();
	            
	            // 既存のトークンが存在する場合は削除または更新する
	            VerificationToken existingToken = verificationTokenService.getVerificationTokenByUser(user);
	            if (existingToken != null) {
	                verificationTokenService.deleteToken(existingToken);
	            }
	            
	            verificationTokenService.create(user, token);
	            	            
	            String resetUrl = "http://" + appHost + "/resetpassword?token=" + token;
	            
	            try {
	                sendEmail(email, resetUrl);
	          
	            } catch (MessagingException e) {
	                e.printStackTrace();
	            }
	        });
	    }
	    
	    private void sendEmail(String to, String resetUrl) throws MessagingException {
	        MimeMessage message = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);
	        helper.setTo(to);
	        helper.setSubject("パスワードリセット");
	        helper.setText("以下のリンクをクリックしてパスワードをリセットしてください\n" + resetUrl, true);
	        mailSender.send(message);	        
	    }
	    
	    // トークン検証
	    public boolean validateResetPasswordToken(String token) {
	        return verificationTokenService.isTokenValid(token);
	    }
	    
	    @Transactional
	    public void updatePassword(PasswordResetDto form) {
	        VerificationToken verificationToken = verificationTokenService.getVerificationToken(form.getToken());
	        if (verificationToken != null) {
	            User user = verificationToken.getUser();
	            user.setPassword(passwordEncoder.encode(form.getNewPassword()));
	            verificationTokenService.deleteToken(verificationToken);
	            userRepository.save(user);
	        } 
	    }
	}