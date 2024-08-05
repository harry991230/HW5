package com.example.demo.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.model.Account;
import com.example.demo.repository.AccountRepository;

@Configuration
public class DataInitialzer {
	
	
	@Autowired
	private AccountRepository accountRepository;

    @Bean
    CommandLineRunner initAccount() {
		return args -> {
			accountRepository.save(
					Account.builder()
						.username("admin")
						.password("admin")
						.email("admin@gmail.com")
						.build()
			);
		};
	}
}
