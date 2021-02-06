package com.savit.mycassa.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import com.savit.mycassa.dto.SessionData;
import com.savit.mycassa.dto.SessionsData;
import com.savit.mycassa.entity.session.Session;
import com.savit.mycassa.entity.session.StatusSession;
import com.savit.mycassa.entity.user.User;
import com.savit.mycassa.entity.user.details.UserDetailsImpl;
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
	

	public SessionData getSessionById(Long sessionId) throws SessionNotFoundException {
		
		Session session =  sessionRepository.findById(sessionId)
					.orElseThrow(() -> new SessionNotFoundException("Session with id %d doesn't exists"));
		
		return new SessionData(session.getId(), session.getStartedAt(), session.getStatusSession().name());
	}

	
	
	@org.springframework.transaction.annotation.Transactional(	propagation=Propagation.REQUIRED, rollbackFor = {Exception.class})
	public SessionData createNewSession() {
		
		UserDetailsImpl userDetails = userService.getAuthUserDetails();
		Session session = sessionRepository.save(new Session(LocalDateTime.now(), StatusSession.OPENED));
		User user = userRepository.findByEmail(userDetails.getUsername()).get();
		session.setUser(user);
		
		return SessionData.builder().id(session.getId())
				.status(session.getStatusSession().name())
				.startedAt(session.getStartedAt()).build();
	}
	
	public SessionsData getAllAuthUserOpenedSessions() {
		
		UserDetailsImpl userDetails = userService.getAuthUserDetails();
		
		return new SessionsData(sessionRepository.findListNotClosedByUserId(userDetails.getId()));	
		
	}
	

}
