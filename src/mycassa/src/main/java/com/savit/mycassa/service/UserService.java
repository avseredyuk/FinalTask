package com.savit.mycassa.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.savit.mycassa.dto.UserData;
import com.savit.mycassa.dto.UsersData;
import com.savit.mycassa.entity.user.Role;
import com.savit.mycassa.entity.user.User;
import com.savit.mycassa.entity.user.details.UserDetailsImpl;
import com.savit.mycassa.repository.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

	private static final Logger log = LoggerFactory.getLogger(UserDetailsService.class);

	@Autowired
	private final UserRepository userRepository;

	@Autowired
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public Optional<UserData> getPrincipal() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication.getPrincipal() instanceof UserDetailsImpl)) {
			return Optional.empty();
		} 
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		log.info("getPrincipal USER: {}", userDetails.toString());
		UserData ud = UserData.builder()
				.firstName(userDetails.getFirstName())
				.lastName(userDetails.getLastName())
				.email(userDetails.getUsername())
				.role(userDetails.getRole().name()).build();
		
		return Optional.of(ud);
	}

	@Override
	public UserDetails loadUserByUsername(String email) {

		return new UserDetailsImpl(userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException(String.format("email %s not found", email))));

	}

	public void signUpUser(UserData userData) {

		Optional<User> us = userRepository.findByEmail(userData.getEmail());

		boolean isPresent = us.isPresent();

		if (isPresent) {
			log.info(" >> such user is exists: {}", us.get().toString());
			throw new IllegalStateException("===== user with such email is exists =====	");
		}

		String encPassword = bCryptPasswordEncoder.encode(userData.getPassword());

		User user = new User(userData.getEmail(), encPassword, userData.getFirstName(), userData.getLastName(),
				Role.valueOf(userData.getRole()));

		userRepository.save(user);
		log.info(" >> new user: {}", user.toString());

	}

    public UsersData getAllUsersCashiers() {
        return new UsersData(userRepository.findByRole(Role.CASHIER));
        
    }

}
