package com.savit.mycassa.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.savit.mycassa.dto.UserData;
import com.savit.mycassa.entity.user.Role;
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
//
//	@Autowired
//	private final EmailValidator emailValidator;

	@Override
	public UserDetails loadUserByUsername(String email) {

		return new UserDetailsImpl(userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException(String.format("email %s not found", email))));

	}

	public void signUpUser(UserData userData) {

//		boolean isValidEmail = emailValidator.test(userDTO.getEmail());
//
//		if (!isValidEmail) {
//			throw new IllegalStateException("email is not valid");
//		}

		boolean isPresent = userRepository.findByEmail(userData.getEmail()).isPresent();

		if (!isPresent) {
			throw new IllegalStateException("user with such email is exists");
		}

		String encPassword = bCryptPasswordEncoder.encode(userData.getPassword());

		User user = new User(userData.getEmail(), encPassword, userData.getFirstName(), userData.getLastName(),
				Set.of(new Role(3, "CASHIER")));

	}

	public List<User> getUsers() {
		return userRepository.findAll();
	}

}
