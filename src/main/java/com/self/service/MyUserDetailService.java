package com.self.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.self.dao.MyUserRepository;
import com.self.model.MyUser;

@Service
public class MyUserDetailService implements UserDetailsService {

	@Autowired
	private MyUserRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<MyUser> user= repository.findByUsername(username);
		
		if(user.isPresent())
		{
			var userobj=user.get();
			return User.builder().username(userobj.getUsername())
					.password(userobj.getPassword())
					.build();
		}
		else
		{
			throw new UsernameNotFoundException(username);
		}
		 
		 
	}

}
