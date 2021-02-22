package com.savit.mycassa.service;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.savit.mycassa.dto.ShiftDTO;
import com.savit.mycassa.entity.session.Session;
import com.savit.mycassa.entity.session.StatusSession;
import com.savit.mycassa.entity.shift.Shift;
import com.savit.mycassa.entity.shift.StatusShift;
import com.savit.mycassa.repository.SessionRepository;
import com.savit.mycassa.repository.ShiftRepository;
import com.savit.mycassa.util.exception.CantPrintCheckException;
import com.savit.mycassa.util.exception.OpenedShiftAlreadyExists;
import com.savit.mycassa.util.exception.OpenedShiftNotExistsException;
import com.savit.mycassa.util.exception.ShiftCloseException;
import com.savit.mycassa.util.pdf.CheckBuilder;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class ShiftService {

	@Autowired
	private final ShiftRepository shiftRepository;	
	@Autowired
	private final SessionRepository sessionRepository;
	
	


	@Transactional
	public ShiftDTO getOpenedShift() {

		Shift shift =  shiftRepository.findByStatusShift(StatusShift.OPENED).orElseThrow(OpenedShiftNotExistsException::new);

		return ShiftDTO.builder()
				.id(shift.getId())
				.status(shift.getStatusShift().name())
				.startedAt(shift.getStartedAt()).build();
		
	}
	
	
	@Transactional
	public ShiftDTO createAndGetNewShift() {

		Optional<Shift> shift =  shiftRepository.findByStatusShift(StatusShift.OPENED);
	
		//if shift already opened
		if (!shift.isEmpty()) {
			throw new OpenedShiftAlreadyExists();
		}

		//if shift not opened yet
		Shift newShift = shiftRepository.save(Shift.builder()
				.startedAt(LocalDateTime.now())
				.statusShift(StatusShift.OPENED).build());

		return ShiftDTO.builder()
				.id(newShift.getId())
				.status(newShift.getStatusShift().name())
				.startedAt(newShift.getStartedAt()).build();
	}

	@Transactional
	public ShiftDTO closeAndGetShift(Long shiftId) {
	
		if(sessionRepository.findCountByShiftIdAndNotEnded(shiftId) != 0) {
			throw new ShiftCloseException();
		}
		
		Shift shift =  shiftRepository.findByIdAndStatusShift(shiftId, StatusShift.OPENED).orElseThrow(OpenedShiftNotExistsException::new);		
		
		shift.setEndedAt(LocalDateTime.now());
		shift.setStatusShift(StatusShift.CLOSED);
		return new ShiftDTO(shift.getId(), shift.getStartedAt(), shift.getEndedAt(), shift.getStatusShift().name());
	}


	public ByteArrayInputStream getReportX(Long shiftId) throws CantPrintCheckException {
		
		Shift shift =  shiftRepository.findByIdAndStatusShift(shiftId, StatusShift.OPENED).orElseThrow(OpenedShiftNotExistsException::new);		
		
		List<Session> closedSessions = shift.getSessions().stream()
				.filter(a -> StatusSession.CLOSED.equals(a.getStatusSession()))
				.collect(Collectors.toList());
		
		Long shiftAmount = getTotalAmount(closedSessions);
		return CheckBuilder.buildXReport(shift, closedSessions, shiftAmount);
	}
	
	
	public ByteArrayInputStream getReportZ(Long shiftId) throws CantPrintCheckException {
		
		Shift shift =  shiftRepository.findById(shiftId).orElseThrow(OpenedShiftNotExistsException::new);		
		Long shiftAmount = getTotalAmount(shift.getSessions());
		Long nonZeroAmount = getTotalAmount(sessionRepository.findByStatusSession(StatusSession.CLOSED));
		
		return CheckBuilder.buildZReport(shift, shift.getSessions(),shiftAmount,  nonZeroAmount);
	}
	
	
	private Long getTotalAmount(List<Session> sessions) {
		Long totalAmount = 0L;
		for(Session session : sessions) {
			totalAmount += session.getSales().stream()
					.map(a -> a.getFixedPrice()*a.getQuantity())
					.reduce(0L, (partialAmountPrice, currentPrice) -> partialAmountPrice + currentPrice);
		}
		return totalAmount;
	}
	
}
