package com.savit.mycassa.service;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.savit.mycassa.dto.SaleDTO;
import com.savit.mycassa.dto.SalesDTO;
import com.savit.mycassa.entity.product.Product;
import com.savit.mycassa.entity.product.Sale;
import com.savit.mycassa.entity.session.Session;
import com.savit.mycassa.entity.session.StatusSession;
import com.savit.mycassa.repository.ProductRepository;
import com.savit.mycassa.repository.SaleRepository;
import com.savit.mycassa.repository.SessionRepository;
import com.savit.mycassa.util.exception.CantPrintCheckException;
import com.savit.mycassa.util.exception.EmptySalesListException;
import com.savit.mycassa.util.exception.NoOpenedSessionException;
import com.savit.mycassa.util.exception.SessionNotStartedYetException;
import com.savit.mycassa.util.pdf.CheckBuilder;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class SaleService {
	
	
	
	@Autowired
	private final UserService userService;
	
	
	@Autowired
	private final SaleRepository saleRepository;
	
	@Autowired
	private final ProductRepository productRepository;
	
	@Autowired
	private final SessionRepository sessionRepository;

	@Transactional(rollbackFor = Exception.class)
	public Sale newProductSale(String ean, SaleDTO saleDTO) {
		
		Product product = productRepository.findByEan(ean).get();
		
		Session session = sessionRepository.findByUserIdAndByStatus(userService.getPrincipalUser().getId(), StatusSession.OPENED).get();
		
		product.setQuantityInStore(product.getQuantityInStore() - saleDTO.getQuantityToBuy());
		
		return saleRepository.save(new Sale(saleDTO.getQuantityToBuy(), product, session));
		
		
	}

	public SalesDTO getOpenedSessionSales() throws SessionNotStartedYetException {
		
		Optional <Session> session = sessionRepository.findByUserIdAndNotEnded(userService.getPrincipalUser().getId());
		
		if(session.isEmpty()) {
			throw new SessionNotStartedYetException("You have not started a session yet");
		}
		
		List<Sale> sales = saleRepository.findAllBySessionIdAndByUserId(session.get().getId());
		
		Long totalPrice = 0L;
		
		for(Sale sale : sales) {
			totalPrice+=sale.getQuantity() * sale.getProduct().getCost();			
		}

		return new SalesDTO(sales, totalPrice );
	}

	@Transactional
	public ByteArrayInputStream getCheck() throws SessionNotStartedYetException, EmptySalesListException, CantPrintCheckException {
		
		Optional<Session> session = sessionRepository.findByUserIdAndByStatus(userService.getPrincipalUser().getId(), StatusSession.OPENED);
		
		if(session.isEmpty()) {
			throw new SessionNotStartedYetException("You have not started a session yet");
		}
		
		session.get().setEndedAt(LocalDateTime.now());
		session.get().setStatusSession(StatusSession.CLOSED);
		
		return   CheckBuilder.buildSessionPDFCheck(session.get());

	}
	
//
//	public SessionData addSale(UserData userData) {
//		
//
//		Optional<User> us = userRepository.findByEmail(userData.getEmail());
//
//		boolean isPresent = us.isPresent();
//
//		if (!isPresent) {
//			log.error(" >> such user isn't exists: {}", us.get().toString());
//			throw new IllegalStateException("===== user with such email isn't exists =====	");
//		}
//
//		Session session = sessionRepository.save(new Session(LocalDateTime.now(), StatusSession.OPENED));
//		
//		log.info(" >> new user: {}", session.toString());
//		
//		return new SessionData(session.getId(), session.getStartedAt(), session.getStatusSession().name());
//	}
	

}
