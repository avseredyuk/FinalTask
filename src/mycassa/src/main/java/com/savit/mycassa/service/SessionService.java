package com.savit.mycassa.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.savit.mycassa.dto.SessionData;
import com.savit.mycassa.dto.SessionsData;
import com.savit.mycassa.entity.session.Session;
import com.savit.mycassa.entity.session.StatusSession;
import com.savit.mycassa.entity.user.User;
import com.savit.mycassa.repository.SessionRepository;
import com.savit.mycassa.repository.UserRepository;
import com.savit.mycassa.util.exception.NoOpenedSessionException;
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
	public SessionData createNewSession() throws Exception {
		
		
		User userAuth = userService.getPrincipalUser();
		//TODO: replace titles prop
		if(sessionRepository.findCountByUserIdAndNotEnded(userAuth.getId()) > 0) {
			throw new Exception();
		}
		
		Session session = sessionRepository.save(new Session(LocalDateTime.now(), StatusSession.OPENED));
		
		session.setUser(userRepository.findByEmail(userAuth.getEmail()).get());
		
		return SessionData.builder().id(session.getId())
				.status(session.getStatusSession().name())
				.startedAt(session.getStartedAt()).build();
	}
	
//	public SessionsData getAllAuthUserOpenedSessions() {
//		
//		return new SessionsData(sessionRepository.findListNotClosedByUserId(userService.getPrincipalUser().getId()));	
//		
//	}
	
	public SessionData getAuthUserOpenedSession() throws NoOpenedSessionException {
		
		Optional<Session> session = sessionRepository.findByUserIdAndNotEnded(userService.getPrincipalUser().getId());
		
		if(session.isEmpty()) {
			throw new NoOpenedSessionException("not.opened.sessions.exception");
		}
		
		
		return SessionData.builder().id(session.get().getId())
				.status(session.get().getStatusSession().name())
				.startedAt(session.get().getStartedAt()).build();
		
	}

//FIXME is @Transactional need?
	@Transactional
	public void updateStatusSession(StatusSession status) throws NoOpenedSessionException  {
		Optional<Session> session = sessionRepository.findByUserIdAndNotEnded(userService.getPrincipalUser().getId());

		session.get().setStatusSession(status);
		
	}



	public SessionsData getSessionsByStatus(StatusSession waiting) {
		
		return new SessionsData(sessionRepository.findByStatusSession(waiting));
	}
	

}
