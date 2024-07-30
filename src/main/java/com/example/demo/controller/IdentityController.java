package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.Account;
import com.example.demo.repository.AccountRepository;

@Controller
public class IdentityController {


    @Autowired
    public AccountRepository accountRepository;

   

    @GetMapping("/")
    public String index(Model model) {
    	
        model.addAttribute("account", new Account());
        
        return "index";
    }

   
    
    @PostMapping("/")
    public String login(@ModelAttribute Account account, Model model) {
    	
    	String username = account.getUsername();
    	
    	Optional<Account> acc = accountRepository.findByUsername(username);

    	if (!acc.isEmpty()) {
        	
        	return "redirect:/home/";
        }
        
        return "index";
    }
}
