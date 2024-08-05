package com.example.demo.service;

import com.example.demo.dto.UserLoginRequest;

public interface UserService {
	
	public boolean authenticateUser(UserLoginRequest userLoginRequest);
	
}
