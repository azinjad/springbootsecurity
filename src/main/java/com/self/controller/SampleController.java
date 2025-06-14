package com.self.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.self.dao.MyUserRepository;
import com.self.model.LoginForm;
import com.self.model.MyUser;
import com.self.service.JwtService;
import com.self.service.MyUserDetailService;

@RestController
public class SampleController {

	@Autowired
	private MyUserRepository myUserRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private MyUserDetailService myUserDetailService;
	
	@GetMapping("/home")
	public String getHome()
	{
		return "homes";
	}
	
	@GetMapping("/admin")
	public String getAdmin()
	{
		return "admin";
	}
	
	@PostMapping("/register/user")
	public MyUser createUser(@RequestBody MyUser user)
	{
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		return myUserRepository.save(user);
	}
	
	@PostMapping("/authenticate")
	public String authenticateAndGetToken(@RequestBody LoginForm loginForm)
	{
	Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.username(), loginForm.password()));
		
		if(authentication.isAuthenticated())
		{
		return jwtService.generateToken(myUserDetailService.loadUserByUsername(loginForm.username()));
			
		}else
		{
			throw new UsernameNotFoundException("Invalid Credential");
		}
		
	}

}
