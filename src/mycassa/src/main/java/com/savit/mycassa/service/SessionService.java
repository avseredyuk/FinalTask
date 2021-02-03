package com.savit.mycassa.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.savit.mycassa.dto.SessionData;
import com.savit.mycassa.dto.UserData;
import com.savit.mycassa.entity.session.Session;
import com.savit.mycassa.entity.session.StatusSession;
import com.savit.mycassa.entity.user.User;
import com.savit.mycassa.repository.SessionRepository;
import com.savit.mycassa.repository.UserRepository;
import com.savit.mycassa.util.exception.SessionNotFoundException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class SessionService {
	
	@Autowired
	private final SessionRepository sessionRepository;
	
	@Autowired
	private final UserRepository userRepository;
	
	@Autowired
	private final UserService userService;
	

	public SessionData addSession(UserData userData) {
		

		Optional<User> us = userRepository.findByEmail(userData.getEmail());

		boolean isPresent = us.isPresent();

		if (!isPresent) {
			log.error(" >> such user isn't exists: {}", us.get().toString());
			throw new IllegalStateException("===== user with such email isn't exists =====	");
		}

		Session session = sessionRepository.save(new Session(LocalDateTime.now(), StatusSession.OPENED));
		
		log.info(" >> new user: {}", session.toString());
		
		return new SessionData(session.getId(), session.getStartedAt(), session.getStatusSession().name());
	}


	public SessionData getSessionById(Long sessionId) throws SessionNotFoundException {
		
		Session session =  sessionRepository.findById(sessionId)
					.orElseThrow(() -> new SessionNotFoundException("Session with id %d doesn't exists"));
		
		return new SessionData(session.getId(), session.getStartedAt(), session.getStatusSession().name());
	}
	
	

}
