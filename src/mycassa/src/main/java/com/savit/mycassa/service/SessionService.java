package com.savit.mycassa.service;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.savit.mycassa.dto.CheckDTO;
import com.savit.mycassa.dto.SessionDTO;
import com.savit.mycassa.dto.SessionsDTO;
import com.savit.mycassa.dto.TimeBordersDTO;
import com.savit.mycassa.entity.product.Sale;
import com.savit.mycassa.entity.session.Session;
import com.savit.mycassa.entity.session.StatusSession;
import com.savit.mycassa.entity.shift.Shift;
import com.savit.mycassa.entity.shift.StatusShift;
import com.savit.mycassa.repository.SaleRepository;
import com.savit.mycassa.repository.SessionRepository;
import com.savit.mycassa.repository.ShiftRepository;
import com.savit.mycassa.repository.UserRepository;
import com.savit.mycassa.util.exception.CantPrintCheckException;
import com.savit.mycassa.util.exception.NotClosedSessionExistsException;
import com.savit.mycassa.util.exception.OpenedShiftNotExistsException;
import com.savit.mycassa.util.exception.SaleNotExistsException;
import com.savit.mycassa.util.exception.SessionNotStartedYetException;
import com.savit.mycassa.util.exception.UserNotFoundException;
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
	
	@Autowired
	private final ShiftRepository shiftRepository;

	public SessionDTO getSessionById(Long sessionId) throws SessionNotStartedYetException {

		Session session = sessionRepository.findById(sessionId).orElseThrow(SessionNotStartedYetException::new);

		return new SessionDTO(session.getId(), session.getStartedAt(), session.getStatusSession().name());
	}

	@Transactional
	public SessionDTO createNewSessionAuth(UserDetails userDetails) throws NotClosedSessionExistsException {

		if (sessionRepository.findByUserEmailAndNotEnded(userDetails.getUsername()).isPresent()) {
			throw new NotClosedSessionExistsException();
		}

		Session session = sessionRepository.save(Session
				.builder()
				.startedAt(LocalDateTime.now())
				.statusSession(StatusSession.OPENED)
				.shift(shiftRepository.findByStatusShift(StatusShift.OPENED).orElseThrow(OpenedShiftNotExistsException::new))
				.user(userRepository.findByEmail(userDetails.getUsername()).orElseThrow(UserNotFoundException::new))
				.build());

		SessionDTO dto =  SessionDTO.builder().id(session.getId()).status(session.getStatusSession().name())
				.startedAt(session.getStartedAt()).build();
		
		return dto;
		
//		return SessionDTO.builder().id(session.getId()).status(session.getStatusSession().name())
//				.startedAt(session.getStartedAt()).build();
	}

	public SessionDTO getNotEndedSessionAuth(UserDetails userDetails) {

		Session session = sessionRepository.findByUserEmailAndNotEnded(userDetails.getUsername())
				.orElseThrow(SessionNotStartedYetException::new);

		return SessionDTO.builder().id(session.getId()).status(session.getStatusSession().name())
				.startedAt(session.getStartedAt()).build();

	}

	public void updateStatusSessionAuth(UserDetails userDetails, StatusSession status) {

		Session session = sessionRepository.findByUserEmailAndNotEnded(userDetails.getUsername())
				.orElseThrow(SessionNotStartedYetException::new);

		session.setStatusSession(status);

	}

	@Transactional
	public void cancelSession(Long session_id) {

		Session session = sessionRepository.findById(session_id).orElseThrow(SessionNotStartedYetException::new);

		List<Sale> sales = session.getSales();
		saleRepository.deleteAll(sales);

		session.setEndedAt(LocalDateTime.now());
		session.setStatusSession(StatusSession.CLOSED);

		for (Sale sale : sales) {
			sale.getProduct().setQuantityInStore(sale.getProduct().getQuantityInStore() + sale.getQuantity());
		}

	}

	@Transactional
	public ByteArrayInputStream getCheck(Long session_id) throws CantPrintCheckException{

		Session session = sessionRepository.findById(session_id).orElseThrow(SessionNotStartedYetException::new);
		session.setEndedAt(LocalDateTime.now());
		session.setStatusSession(StatusSession.CLOSED);
		
		Long totalSessionAmount = session.getSales().stream()
				.map(a -> a.getFixedPrice()*a.getQuantity())
				.reduce(0L, (partial, current) -> partial + current);


		return CheckBuilder.buildSessionPDFCheck(session, session.getSales(), totalSessionAmount);//FIXME


	}

	public CheckDTO getCheckDTO(Long sessionId) {

		Session session = sessionRepository.findById(sessionId).orElseThrow(SessionNotStartedYetException::new);

		if (StatusSession.CLOSED.equals(session.getStatusSession())) {
			throw new SessionNotStartedYetException();
		}

		Long totalPrice = 0L;
		List<Sale> sales = session.getSales();
		
		// TODO: remake to stream:
		for (Sale sale : sales) {
			totalPrice += sale.getQuantity() * sale.getFixedPrice();
		}

		return new CheckDTO(String.valueOf(session.getId()), session.getStartedAt(), session.getStatusSession().name(),
				sales, totalPrice);

	}

	@Transactional
	public void deleteSaleFromCheck(Long sessionId, Long saleId) {

		saleRepository.delete(saleRepository.findById(saleId).orElseThrow(SaleNotExistsException::new));

		Session session = sessionRepository.findById(sessionId).orElseThrow(SessionNotStartedYetException::new);

		session.setStatusSession(StatusSession.OPENED);
	}

	public SessionsDTO getSessionsByStatus(StatusSession status) {
		return new SessionsDTO(sessionRepository.findByStatusSession(status));
	}

	public SessionsDTO getSessionsForStatistics(TimeBordersDTO borders) {
		List<Session> sessions = sessionRepository.findByStatusSessionAndByTimeBorders(StatusSession.CLOSED,
				borders.getTimeFrom(), borders.getTimeTo());
		return new SessionsDTO(sessions);
	}

	public ByteArrayInputStream getSessionReportDocAuth(UserDetails userDetails, SessionsDTO sessionsDTO)
			throws UserNotFoundException, Exception {
//		return CheckBuilder.buildSessionsWithTimeBordersPdfReport(
//				userRepository.findByEmail(userDetails.getUsername()).orElseThrow(UserNotFoundException::new),
//				sessionsDTO.getSessions());
		return null;//FIXME
	}

}
