package com.example.nagoyameshi.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.nagoyameshi.entity.Company;
import com.example.nagoyameshi.entity.Terms;
import com.example.nagoyameshi.form.CompanyEditForm;
import com.example.nagoyameshi.form.TermsEditForm;
import com.example.nagoyameshi.repository.CompanyRepository;
import com.example.nagoyameshi.repository.TermsRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/com")
public class CompanyController {
	private final CompanyRepository companyRepository;
	private final TermsRepository termsRepository;
	 
	 public CompanyController(CompanyRepository companyRepository, TermsRepository termsRepository) {
		 this.companyRepository = companyRepository;
		 this.termsRepository = termsRepository;
	 }
	
    @GetMapping
    public String com(Model model) {
   	Company com = companyRepository.findById(1).orElse(null);
	model.addAttribute("com", com);
   	 
   	 return "company/com";
    }
	 @PreAuthorize("hasRole('ROLE_ADMIN')")
	    @GetMapping("/{id}/edit")
	    public String edit(@PathVariable(name = "id") Integer id, Model model) {
	        Company com = companyRepository.findById(id).orElse(null);
	        CompanyEditForm companyEditForm = new CompanyEditForm(
	            com.getId(), 
	            com.getCompanyName(), 
	            com.getName(), 
	            com.getPostalCode(), 
	            com.getAddress(), 
	            com.getBusinessContent()
	        );
	        
	        model.addAttribute("companyEditForm", companyEditForm);
	        return "company/edit";
	    }

	    @PreAuthorize("hasRole('ROLE_ADMIN')")
	    @PostMapping("/{id}/edit")
	    public String update(@PathVariable(name = "id") Integer id, @Valid CompanyEditForm form, BindingResult result) {
	        if (result.hasErrors()) {
	            return "company/edit";
	        }
	        
	        Company com = companyRepository.findById(id).orElse(null);
	        com.setCompanyName(form.getCompanyName());
	        com.setName(form.getName());
	        com.setPostalCode(form.getPostalCode());
	        com.setAddress(form.getAddress());
	        com.setBusinessContent(form.getBusinessContent());
	        
	        companyRepository.save(com);
	        
	        return "redirect:/com";
	    
	}
	
	    @GetMapping("/terms")
	    public String terms(Model model) {
	   	Terms terms = termsRepository.findById(1).orElse(null);
		model.addAttribute("terms", terms);
	   	 
	   	 return "company/terms";
	    }
	    
		 @PreAuthorize("hasRole('ROLE_ADMIN')")
		    @GetMapping("/terms/{id}/termsedit")
		    public String termsEdit(@PathVariable(name = "id") Integer id, Model model) {
		        Terms terms = termsRepository.findById(id).orElse(null);
		        TermsEditForm termsEditForm = new TermsEditForm(
		            terms.getId(), 
		            terms.getArticle1(), 
		            terms.getArticle2(), 
		            terms.getArticle3()
		       );
		        
		        model.addAttribute("termsEditForm", termsEditForm);
		        return "company/termsedit";
		    }

		    @PreAuthorize("hasRole('ROLE_ADMIN')")
		    @PostMapping("/terms/{id}/termsedit")
		    public String termsUpdate(@PathVariable(name = "id") Integer id, @Valid TermsEditForm form, BindingResult result) {
		        if (result.hasErrors()) {
		            return "company/termsedit";
		        }
		        
		        Terms terms = termsRepository.findById(id).orElse(null);
		        terms.setArticle1(form.getArticle1());
		        terms.setArticle2(form.getArticle2());
		        terms.setArticle3(form.getArticle3());
		        
		        termsRepository.save(terms);
		        
		        return "redirect:/com/terms";
		    
		}    
}

