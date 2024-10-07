package com.example.nagoyameshi.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.entity.VerificationToken;
import com.example.nagoyameshi.repository.VerificationTokenRepository;

@Service
public class VerificationTokenService {
	private final VerificationTokenRepository verificationTokenRepository;

    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {        
        this.verificationTokenRepository = verificationTokenRepository;
    } 
    
    @Transactional
    public void create(User user, String token) {
        VerificationToken existingToken = verificationTokenRepository.findByUser(user);
        
        if (existingToken != null) {
            // 既存のトークンがある場合は更新する
            existingToken.setToken(token);
            existingToken.setExpiryDate(Timestamp.from(Instant.now().plus(1, ChronoUnit.DAYS))); 
            verificationTokenRepository.save(existingToken);
        } else {
            // 既存のトークンがない場合は新しく作成する
            VerificationToken verificationToken = new VerificationToken();
            verificationToken.setUser(user);
            verificationToken.setToken(token); 
            verificationToken.setExpiryDate(Timestamp.from(Instant.now().plus(1, ChronoUnit.DAYS))); 
            verificationTokenRepository.save(verificationToken);
        }
    }
        
    // トークンの文字列で検索した結果を返す
    public VerificationToken getVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }
    
 // トークンをユーザーから取得する
    public VerificationToken getVerificationTokenByUser(User user) {
        return verificationTokenRepository.findByUser(user);
    }

    // トークンの有効性を検証する
    public boolean isTokenValid(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken != null && verificationToken.getExpiryDate().after(Timestamp.from(Instant.now()))) {
            return true;
        }
        return false;
    }

    @Transactional
    public void deleteToken(VerificationToken verificationToken) {
        verificationTokenRepository.delete(verificationToken);
    }

    @Transactional
    public void update(User user, String newToken) {
        VerificationToken verificationToken = getVerificationTokenByUser(user);
        if (verificationToken != null) {
            verificationToken.setToken(newToken);
            verificationToken.setExpiryDate(Timestamp.from(Instant.now().plus(1, ChronoUnit.DAYS))); 
            verificationTokenRepository.save(verificationToken);
        } else {
            create(user, newToken); // トークンがない場合は新しく作成
        }
    }
}
