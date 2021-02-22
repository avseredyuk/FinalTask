package com.savit.mycassa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.savit.mycassa.dto.UserDTO;
import com.savit.mycassa.entity.user.Role;
import com.savit.mycassa.entity.user.User;
import com.savit.mycassa.entity.user.details.UserDetailsImpl;
import com.savit.mycassa.repository.UserRepository;
import com.savit.mycassa.util.exception.UserNotFoundException;

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

	@Override
	public UserDetails loadUserByUsername(String email) {

		return new UserDetailsImpl(userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException(String.format("email %s not found", email))));

	}

	public UserDTO getUserByEmailAuth(UserDetails userDetails) throws UserNotFoundException {

		User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(UserNotFoundException::new);

		return UserDTO.builder().firstName(user.getFirstName()).lastName(user.getLastName()).email(user.getEmail())
				.role(user.getRole().name()).build();
	}

	@Transactional
	public void signUpUser(UserDTO userDTO) {

		String encPassword = bCryptPasswordEncoder.encode(userDTO.getPassword());

		User user = new User(userDTO.getEmail(), encPassword, userDTO.getFirstName(), userDTO.getLastName(),
				Role.valueOf(userDTO.getRole()));

		userRepository.save(user);
	}

	@Transactional
	public void updateUser(UserDTO userDTO, UserDetails userDetails) throws Exception {

		User userToUpdate = userRepository.findByEmail(userDetails.getUsername())
				.orElseThrow(UserNotFoundException::new);

		userRepository.save(User.builder().id(userToUpdate.getId()).email(userDTO.getEmail())
				.firstName(userDTO.getFirstName()).lastName(userDTO.getLastName())
				.password(bCryptPasswordEncoder.encode(userDTO.getPassword())).role(userToUpdate.getRole()).build());
		

	}

}
