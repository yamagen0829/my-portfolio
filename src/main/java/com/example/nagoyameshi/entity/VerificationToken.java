package com.example.nagoyameshi.entity;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "verification_tokens")
@Data
public class VerificationToken {
	 @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "id")
     private Integer id;
     
     @OneToOne
     @JoinColumn(name = "user_id")
     private User user;    
     
     @Column(name = "token")
     private String token;
     
     @Column(name = "expiry_date")
     private Timestamp expiryDate;
     
     @Column(name = "created_at", insertable = false, updatable = false)
     private Timestamp createdAt;
     
     @Column(name = "updated_at", insertable = false, updatable = false)
     private Timestamp updatedAt; 
     
  // デフォルトのコンストラクタでexpiryDateを1日後に設定
     public VerificationToken() {
         this.expiryDate = Timestamp.from(Instant.now().plus(1, ChronoUnit.DAYS));
     }
}
