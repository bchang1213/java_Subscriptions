package com.brianchang.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	//Dependency Injection of details services
	private UserDetailsService userDetailsService;
	    
	public WebSecurityConfig(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
	
	// add Bcrypt bean
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	//helps with logout timer.
	@Autowired
	CustomLogoutSuccessHandler customLogoutSuccessHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.
			authorizeRequests()
				.antMatchers("/css/**", "/js/**", "/registration").permitAll()
				.antMatchers("/admin/**").access("hasRole('ADMIN')")
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
			.logout().logoutSuccessHandler(customLogoutSuccessHandler)
				.permitAll();
		http.exceptionHandling().accessDeniedPage("/403");

	}
	
	// 1
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}

}
