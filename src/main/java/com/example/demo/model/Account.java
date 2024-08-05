package com.example.demo.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.util.DigestUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    
    @Column(nullable = false)
    private String password;

    
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    
    @CreationTimestamp
    private LocalDateTime creationDateTime;

    
    @Builder.Default
    private boolean isEnabled = Boolean.TRUE;
    
    
    
    
    public static class AccountBuilder {
    	

		public AccountBuilder password(String pwd) {
    		
    		this.password = hashPassword(pwd);
    		
    		return this;
    	}
    	
    	
    	public String hashPassword(String pwd) {
    		
    		return DigestUtils.md5DigestAsHex(pwd.getBytes());
    		
    	}
    }
    
}

