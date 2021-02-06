package com.savit.mycassa.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
		
		Session session = sessionRepository.findByUserIdAndByStatus(userService.getAuthUserDetails().getId(), StatusSession.OPENED).get();
		
		product.setQuantityInStore(product.getQuantityInStore() - saleDTO.getQuantityToBuy());
		
//		Optional <Product> existedProduct = session.getSales()
//												.stream()
//												.filter(sale -> sale.getProduct().getId().equals(product.getId()))
//												.findAny()
//												.map(sale -> sale.getProduct());
//		//TODO: bg  E8F2FE
//		if(existedProduct.isPresent()) {
//			return saleRepository.save(new Sale(saleDTO.getQuantityToBuy(), existedProduct.get(), session));
//		}

		return saleRepository.save(new Sale(saleDTO.getQuantityToBuy(), product, session));
		
		
	}

	public SalesDTO getAllSessionSales() {
		
		Session session = sessionRepository.findOneNotClosedByUserId(userService.getAuthUserDetails().getId()).get();
		
		List<Sale> sales = saleRepository.findAllBySessionIdAndByUserId(session.getId());
		
		Long totalPrice = 0L;
		
		for(Sale sale : sales) {
			totalPrice+=sale.getQuantity() * sale.getProduct().getCost();			
		}

		return new SalesDTO(sales, totalPrice );
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
