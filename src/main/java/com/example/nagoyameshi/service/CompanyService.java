package com.example.nagoyameshi.service;

import org.springframework.stereotype.Service;

import com.example.nagoyameshi.repository.CompanyRepository;
import com.example.nagoyameshi.repository.TermsRepository;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final TermsRepository termsRepository;
    
    public CompanyService (CompanyRepository companyRepository, TermsRepository termsRepository) {
    	this.companyRepository = companyRepository;
    	this.termsRepository = termsRepository;
    }
    
    
}
