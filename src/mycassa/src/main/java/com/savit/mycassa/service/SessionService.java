package com.savit.mycassa.service;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.savit.mycassa.dto.CheckDTO;
import com.savit.mycassa.dto.SessionDTO;
import com.savit.mycassa.dto.SessionsDTO;
import com.savit.mycassa.entity.product.Sale;
import com.savit.mycassa.entity.session.Session;
import com.savit.mycassa.entity.session.StatusSession;
import com.savit.mycassa.repository.SaleRepository;
import com.savit.mycassa.repository.SessionRepository;
import com.savit.mycassa.repository.UserRepository;
import com.savit.mycassa.util.exception.CantPrintCheckException;
import com.savit.mycassa.util.exception.EmptySalesListException;
import com.savit.mycassa.util.exception.NotClosedSessionExistsException;
import com.savit.mycassa.util.exception.SaleNotExistsException;
import com.savit.mycassa.util.exception.SessionNotFoundException;
import com.savit.mycassa.util.exception.SessionNotStartedYetException;
import com.savit.mycassa.util.pdf.CheckBuilder;

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
	private final SaleRepository saleRepository;

	

	public SessionDTO getSessionById(Long sessionId) throws SessionNotFoundException {
		
		Session session =  sessionRepository.findById(sessionId)
					.orElseThrow(() -> new SessionNotFoundException("Session doesn't exists"));
		
		return new SessionDTO(session.getId(), session.getStartedAt(), session.getStatusSession().name());
	}

	
	
	@org.springframework.transaction.annotation.Transactional(	propagation=Propagation.REQUIRED, rollbackFor = {Exception.class})
	public SessionDTO createNewSessionAuth(UserDetails userDetails) throws NotClosedSessionExistsException {
		
		//TODO: add <titles> html to prop
		if(sessionRepository.findCountByUserEmailAndNotEnded(userDetails.getUsername()) > 0) {
			throw new NotClosedSessionExistsException();
		}
		
		Session session = sessionRepository.save(new Session(LocalDateTime.now(), StatusSession.OPENED));
		
		session.setUser(userRepository.findByEmail(userDetails.getUsername()).get());
		
		return SessionDTO.builder().id(session.getId())
				.status(session.getStatusSession().name())
				.startedAt(session.getStartedAt()).build();
	}
	
//	public SessionsData getAllAuthUserOpenedSessions() {
//		
//		return new SessionsData(sessionRepository.findListNotClosedByUserId(userService.getPrincipalUser().getId()));	
//		
//	}
	
	public SessionDTO getNotEndedSessionAuth(UserDetails userDetails) throws SessionNotStartedYetException {

		Session session = sessionRepository.findByUserEmailAndNotEnded(userDetails.getUsername())
				.orElseThrow(() -> new SessionNotStartedYetException("not.opened.sessions.exception"));

		return SessionDTO.builder().id(session.getId())
				.status(session.getStatusSession().name())
				.startedAt(session.getStartedAt()).build();
		
	}

//FIXME is @Transactional need?
	@Transactional
	public void updateStatusSessionAuth(UserDetails userDetails, StatusSession status) throws SessionNotStartedYetException  {
		
		Session session = sessionRepository.findByUserEmailAndNotEnded(userDetails.getUsername())
											.orElseThrow(() -> new SessionNotStartedYetException("Session not opened"));
		
		session.setStatusSession(status);
		
	}



	public SessionsDTO getSessionsByStatus(StatusSession waiting) {
		return new SessionsDTO(sessionRepository.findByStatusSession(waiting));
	}


	@Transactional
	public void closeSession(Long session_id) {
		
		Session session = sessionRepository.findById(session_id)
				.orElseThrow(() -> new SessionNotStartedYetException("This session not started"));

		List<Sale> sales = session.getSales();
		saleRepository.deleteAll(sales);
		
		session.setEndedAt(LocalDateTime.now());
		session.setStatusSession(StatusSession.CLOSED);
		
		for(Sale sale : sales) {
			sale.getProduct().setQuantityInStore(sale.getProduct().getQuantityInStore() + sale.getQuantity());
		}
		
	}


	@Transactional
	public ByteArrayInputStream getCheck(Long session_id) throws SessionNotStartedYetException, EmptySalesListException, CantPrintCheckException {
		
		Session session = sessionRepository.findById(session_id)
				.orElseThrow(() -> new SessionNotStartedYetException("You have not started a session yet"));
		
		session.setEndedAt(LocalDateTime.now());
		session.setStatusSession(StatusSession.CLOSED);
		
		return   CheckBuilder.buildSessionPDFCheck(session);

	}



	public CheckDTO getCheckDTO(Long sessionId) {
		
		Session session =  sessionRepository.findById(sessionId)
				.orElseThrow(() -> new SessionNotFoundException("Session not found"));
		
		if(StatusSession.CLOSED.equals(session.getStatusSession())) {
			throw new SessionNotStartedYetException("You have not started a session yet");
		}
		
		Long totalPrice = 0L;
		List<Sale> sales = session.getSales();
		//TODO: remake to stream:

		for(Sale sale : sales) {
			totalPrice+=sale.getQuantity() * sale.getFixedPrice();			
		}

		return new CheckDTO(String.valueOf(session.getId()),
							session.getStartedAt(),
							session.getStatusSession().name(),
							sales,
							totalPrice);

	}

	
	@Transactional
	public void deleteSaleFromCheck(Long sessionId, Long saleId) {
		
		saleRepository.delete(saleRepository.findById(saleId)
				.orElseThrow(() -> new SaleNotExistsException("The sale not exists")));
		
		Session session = sessionRepository.findById(sessionId)
				.orElseThrow(() -> new SessionNotStartedYetException("Session not started yet"));

		
		session.setStatusSession(StatusSession.OPENED);
		
	}

}
