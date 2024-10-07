package com.example.nagoyameshi.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.nagoyameshi.service.TotallingsService;

@Controller
@RequestMapping("/admin/aggregation/totallings")
public class AdminTotallingController {
     private final TotallingsService totallingsService;
     
     public AdminTotallingController (TotallingsService totallingsService) {
    	 this.totallingsService = totallingsService;
     }

     private void addTotallingsAttributes(Model model) {
         Integer totalUsers = totallingsService.countAllUsers();
         Integer totalRestaurants = totallingsService.countAllRestaurants();
         Integer freeMembership = totallingsService.countFreeMembership();
         Integer paidMembership = totallingsService.countPaidMembership();
         Integer monthlySales = totallingsService.calculateMonthlySales();

         model.addAttribute("totalUsers", totalUsers);
         model.addAttribute("totalRestaurants", totalRestaurants);
         model.addAttribute("freeMembership", freeMembership);
         model.addAttribute("paidMembership", paidMembership);
         model.addAttribute("monthlySales", monthlySales);
     }

     @GetMapping
     public String totallings(Model model) {
    	 
         addTotallingsAttributes(model);
         
         return "admin/aggregation/totallings";
     }
     
     @GetMapping("/index")
     public String showIndex(Model model) {
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
             addTotallingsAttributes(model);
             model.addAttribute("isAdmin", true);
         } else {
             model.addAttribute("isAdmin", false);
         }
         return "index";
     }
}
