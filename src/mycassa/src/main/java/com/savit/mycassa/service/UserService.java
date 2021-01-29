package com.savit.mycassa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.savit.mycassa.entity.user.User;
import com.savit.mycassa.entity.user.details.UserDetailsImpl;
import com.savit.mycassa.repository.UserRepository;

import lombok.AllArgsConstructor;


@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

	@Autowired
	private final UserRepository userRepository;
	
	@Autowired
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	@Override
	public UserDetails loadUserByUsername(String email){
		
		return new UserDetailsImpl(
				userRepository
					.findByEmail(email)
						.orElseThrow(() -> new UsernameNotFoundException(String.format("email %s not found", email))));
		
	}







	public List<User> getUsers() {
		return userRepository.findAll();
	}

}
