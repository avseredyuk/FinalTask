package com.savit.mycassa.service;

import java.util.Optional;

import javax.validation.Valid;

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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.savit.mycassa.dto.UserData;
import com.savit.mycassa.dto.UsersData;
import com.savit.mycassa.entity.user.Role;
import com.savit.mycassa.entity.user.User;
import com.savit.mycassa.entity.user.details.UserDetailsImpl;
import com.savit.mycassa.repository.UserRepository;
import com.savit.mycassa.util.exception.EmailExistsException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class UserService implements UserDetailsService {

	@Autowired
	private final UserRepository userRepository;

	@Autowired
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public User getPrincipalUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		return userRepository.findById(userDetails.getId()).get();
	}

	public UserData getPrincipalUserDTO() {

		User authUser = getPrincipalUser();

//		log.info("getPrincipal USER: {}", authUser.toString());
		UserData ud = UserData.builder().firstName(authUser.getFirstName()).lastName(authUser.getLastName())
				.email(authUser.getEmail()).role(authUser.getRole().name()).build();

		return ud;
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
			log.error(" >> such user is exists: {}", us.get().toString());
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

//	@Transactional(	propagation=Propagation.REQUIRED, rollbackFor = {Exception.class})
	public void updateUser(@Valid UserData userData) throws Exception {

		User userToUpdate = getPrincipalUser();

		User user = User.builder().id(userToUpdate.getId()).email(userData.getEmail())
				.firstName(userData.getFirstName()).lastName(userData.getLastName())
				.password(bCryptPasswordEncoder.encode(userData.getPassword())).role(userToUpdate.getRole()).build();

		userRepository.save(user);
	}

}
