package com.savit.mycassa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.savit.mycassa.dto.SaleDTO;
import com.savit.mycassa.entity.product.Product;
import com.savit.mycassa.entity.product.Sale;
import com.savit.mycassa.entity.session.Session;
import com.savit.mycassa.entity.session.StatusSession;
import com.savit.mycassa.repository.ProductRepository;
import com.savit.mycassa.repository.SaleRepository;
import com.savit.mycassa.repository.SessionRepository;
import com.savit.mycassa.util.exception.ProductNotFoundException;
import com.savit.mycassa.util.exception.SessionNotStartedYetException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class SaleService {

	@Autowired
	private final SaleRepository saleRepository;
	
	@Autowired
	private final ProductRepository productRepository;
	
	@Autowired
	private final SessionRepository sessionRepository;

	@Transactional(rollbackFor = Exception.class)
	public void newProductSaleAuth(String ean, SaleDTO saleDTO, UserDetails userDetails) {
		
		Product product = productRepository.findByEan(ean)
				.orElseThrow(() -> new ProductNotFoundException("Product not found!")); //TODO: in exHand redirect to /products with alert error
		
		Session session = sessionRepository.findByUserEmailAndByStatus(userDetails.getUsername(), StatusSession.OPENED)
											.orElseThrow(() -> new SessionNotStartedYetException("Session not started!"));
	
		product.setQuantityInStore(product.getQuantityInStore() - saleDTO.getQuantityToBuy());
		
		saleRepository.save(new Sale(saleDTO.getQuantityToBuy(), saleDTO.getFixedPrice(), product, session));
		
	}

//	public SalesDTO getOpenedSessionSales(Optional <Long> session_id) throws SessionNotStartedYetException {
//		Optional <Session> session;
//		if(session_id.isPresent()) {
//			session = sessionRepository.findById(session_id.get());
//		}else {
//			session = sessionRepository.findByUserIdAndNotEnded(userService.getPrincipalUser().getId());
//		}
//		
//		if(session.isEmpty()) {
//			throw new SessionNotStartedYetException("This session not started yet");
//		}
//		
//		List<Sale> sales = saleRepository.findAllBySessionId(session.get().getId());
//		
//		Long totalPrice = 0L;
//		
//		for(Sale sale : sales) {
//			totalPrice+=sale.getQuantity() * sale.getProduct().getCost();			
//		}
//
//		return new SalesDTO(session.get().getStatusSession().name(), sales, totalPrice );
//	}

//	@Transactional
//	public ByteArrayInputStream getCheck() throws SessionNotStartedYetException, EmptySalesListException, CantPrintCheckException {
//		
//		Optional<Session> session = sessionRepository.findByUserIdAndByStatus(userService.getPrincipalUser().getId(), StatusSession.OPENED);
//		
//		if(session.isEmpty()) {
//			throw new SessionNotStartedYetException("You have not started a session yet");
//		}
//		
//		session.get().setEndedAt(LocalDateTime.now());
//		session.get().setStatusSession(StatusSession.CLOSED);
//		
//		return   CheckBuilder.buildSessionPDFCheck(session.get());
//
//	}

	
	
	

//	@Transactional
//	public void deleteAllSales(Long session_id) {
//		
//		
//		
//		
//		saleRepository.deleteAllBySessionId(session_id);
//		
//	}
	
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
