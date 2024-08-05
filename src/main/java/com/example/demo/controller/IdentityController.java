package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.repository.AccountRepository;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class IdentityController {


    @Autowired
    public AccountRepository accountRepository;

   

    @GetMapping(value = { "/", "/login" })
    public String index() {
        
        return "index";
    }

   
    @PostMapping(value = { "/auth/login" })
    public String login() {
    	 
    	return "redirect:/home/";
    }

    
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
    	
        request.getSession().invalidate();
        
        return "redirect:/";
    }

    
}
