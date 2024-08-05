package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.Account;
import com.example.demo.repository.AccountRepository;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = HomeController.URL_HOME)
public class HomeController {
	
	protected final static String URL_HOME = "/home";
	
	@Autowired
    public AccountRepository accountRepository;
	
	
	@GetMapping("/")
	public String index(HttpServletRequest request , Model model) {
		
		List<Account> accounts = accountRepository.findAll();
		
		model.addAttribute("accounts", accounts);
        
        return "home/index";
	}
}
