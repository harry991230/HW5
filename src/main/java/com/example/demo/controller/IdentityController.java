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
//@RequestMapping(AccountController.URL_LOGIN)
public class IdentityController {

//    public static final String URL_LOGIN = "/login";

    @Autowired
    private AccountRepository accountRepository;

   

    @GetMapping("/")
    public String index(Model model) {
    	
        model.addAttribute("account", new Account());
        
        return "index";
    }

   
    
    @PostMapping("/")
    public String login(@ModelAttribute Account account, Model model) {
    	
    	String username = account.getUsername();
    	
    	 Optional<Account> acc = accountRepository.findByUsername(username);

    	if (!acc.isPresent()) {
        	
        	return "redirect:/show/";
        }
        
        return "index";
    }
}
