package com.self;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.self.service.MyUserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	@Autowired
	private MyUserDetailService myUserDetailService;
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception
	{
		return httpSecurity
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(req-> 
		{
			req.requestMatchers("/home","/register/**","/authenticate").permitAll();
			req.anyRequest().authenticated();
		})
				.formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	
//	@Bean
//	public UserDetailsService userDetailService()
//	{
//		UserDetails normaluser=User.builder().username("Akash").password("$2a$12$oYs2PnhYkL28YVOgHks..OL58mVkM/R1XL1Pr7S0uHaY8rG7BmCEi").build();
//		
//		return new InMemoryUserDetailsManager(normaluser);
//	}
	
	@Bean
	public UserDetailsService userDetailsService()
	{
		return myUserDetailService;
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider()
	{
		DaoAuthenticationProvider daoprovider=new DaoAuthenticationProvider();
		
		daoprovider.setUserDetailsService(myUserDetailService);
		daoprovider.setPasswordEncoder(passwordEncoder());
		
		return daoprovider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager()
	{
		return new ProviderManager(authenticationProvider());
	}
	
	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

}
