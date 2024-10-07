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
@Table(name = "terms_of_services")
@Data
public class Terms {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "article_1")
	private String article1;
	
	@Column(name = "article_2")
    private String article2;
 
    @Column(name = "article_3")
    private String article3;
 
    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;
 
    @Column(name = "updated_at", insertable = false, updatable = false)
    private Timestamp updatedAt;
}

