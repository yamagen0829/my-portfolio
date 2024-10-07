package com.example.nagoyameshi;

import lombok.Data;

@Data
public class PasswordResetDto {
     private String token;
     private String newPassword;
     
  // Getter and Setter methods
     public String getNewPassword() { return newPassword; }

     public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

     public String getToken() { return token; }

     public void setToken(String token) { this.token = token; }
 }

