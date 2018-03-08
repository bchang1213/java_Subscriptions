package com.brianchang.web.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brianchang.web.models.Role;
import com.brianchang.web.models.User;
import com.brianchang.web.repositories.UserRepository;

@Service
@Transactional
public class UserDetailsServiceImplementation implements UserDetailsService{
	private UserRepository userRepo;
	
	public UserDetailsServiceImplementation(UserRepository userRepo) {
		this.userRepo = userRepo;
	}
	
	// 1
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepo.findByEmail(email);
	        
		if(user == null) {
			throw new UsernameNotFoundException("Email not found");
		}
        
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthorities(user));
	}
	
	// 2
	private List<GrantedAuthority> getAuthorities(User user){
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for(Role role : user.getRoles()) {
			GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getName());
			authorities.add(grantedAuthority);
		}
		return authorities;
	}	

}
