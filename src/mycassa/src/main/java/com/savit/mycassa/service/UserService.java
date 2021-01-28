package com.savit.mycassa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.savit.mycassa.entity.user.details.UserDetailsImpl;
import com.savit.mycassa.repository.UserRepository;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class UserService implements UserDetailsService {

	@Autowired
	private final UserRepository userRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String email){
		
		return new UserDetailsImpl(
				userRepository
					.findByEmail(email)
						.orElseThrow(() -> new UsernameNotFoundException(String.format("email %s not found", email))));
		
	}

}
