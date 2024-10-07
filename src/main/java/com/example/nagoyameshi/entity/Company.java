package com.example.nagoyameshi.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "company_identifications")
@Data
public class Company {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "company_name")
	private String companyName;
	
	@Column(name = "name")
    private String name;
 
    @Column(name = "postal_code")
    private String postalCode;
 
    @Column(name = "address")
    private String address;
 
    @Column(name = "business_content")
    private String businessContent;
 
    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;
 
    @Column(name = "updated_at", insertable = false, updatable = false)
    private Timestamp updatedAt;
}
