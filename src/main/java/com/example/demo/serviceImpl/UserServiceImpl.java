package com.example.demo.serviceImpl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.example.demo.AuthFilter;
import com.example.demo.dto.UserLoginRequest;
import com.example.demo.model.Account;
import com.example.demo.repository.AccountRepository;
import com.example.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private AccountRepository accountRepository;

	@Override
	public boolean authenticateUser(UserLoginRequest userLoginRequest) {
		
		
		String username = userLoginRequest.getUsername();
		String password = userLoginRequest.getPassword();
		
		Optional<Account> acc = accountRepository.findByUsername(username);
        
        if(acc.isPresent()) {
        	
        	return acc.get().getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()));
        }
		
        logger.info("Permission Denied");
        
		return false;
	}

}
