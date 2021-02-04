package com.savit.mycassa.service;

import java.sql.SQLException;
import java.time.LocalDateTime;

import javax.transaction.Transactional;

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
	

//	public SessionData addSession(UserData userData) {
//		
//		
//		log.info(" >> new user: {}", session.toString());
//		
//		return new SessionData(session.getId(), session.getStartedAt(), session.getStatusSession().name());
//	}


	public SessionData getSessionById(Long sessionId) throws SessionNotFoundException {
		
		Session session =  sessionRepository.findById(sessionId)
					.orElseThrow(() -> new SessionNotFoundException("Session with id %d doesn't exists"));
		
		return new SessionData(session.getId(), session.getStartedAt(), session.getStatusSession().name());
	}

	
	
	@org.springframework.transaction.annotation.Transactional(	propagation=Propagation.REQUIRED, rollbackFor = {Exception.class})
	public SessionData createNewSession() {
		
		UserDetailsImpl userDetails = userService.getAuthUserDetails();
		User user = userRepository.findByEmail(userDetails.getUsername()).get();
//		log.info(" >> get user: {}", user.toString());
		Session session = sessionRepository.save(new Session(LocalDateTime.now(), StatusSession.OPENED));
//		log.info(" >> new session without user: {}", session.toString());
		session.setUser(user);
//		session.getStartedAt().star
//		log.info(" >> new session with user: {}", session.toString());
		return SessionData.builder().id(session.getId())
				.status(session.getStatusSession().name())
				.startedAt(session.getStartedAt()).build();
	}
	
	public SessionsData getAllAuthUserOpenedSessions() {
		
//		log.info(" >> get all sessions method");
		UserDetailsImpl userDetails = userService.getAuthUserDetails();
//		log.info(" >> get userdatails: {}", userDetails.toString());
//		log.info("userDetails: {}, {}", userDetails.getId(), StatusSession.OPENED.name());
		SessionsData sessionsData = 
//				new SessionsData(null);
				new SessionsData(sessionRepository.findByUserAndByStatusSession(userDetails.getId(), StatusSession.OPENED));
//		log.info("SESSIONSDATA: {}", sessionsData);
//		sessionsData.getSessionsData().stream().forEach(a -> System.out.println(a));
				
		return sessionsData;
		
	}
	

}
