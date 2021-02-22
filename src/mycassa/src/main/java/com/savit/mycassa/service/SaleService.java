package com.savit.mycassa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.savit.mycassa.dto.SaleDTO;
import com.savit.mycassa.entity.product.Product;
import com.savit.mycassa.entity.sale.Sale;
import com.savit.mycassa.entity.session.Session;
import com.savit.mycassa.entity.session.StatusSession;
import com.savit.mycassa.repository.ProductRepository;
import com.savit.mycassa.repository.SaleRepository;
import com.savit.mycassa.repository.SessionRepository;
import com.savit.mycassa.util.exception.ProductNotFoundException;
import com.savit.mycassa.util.exception.SaleNotExistsException;
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

	@Transactional
	public void newProductSaleAuth(String ean, SaleDTO saleDTO, UserDetails userDetails) {
		
		Product product = productRepository.findByEan(ean)
				.orElseThrow(ProductNotFoundException::new);
		
		Session session = sessionRepository.findByUserEmailAndByStatus(userDetails.getUsername(), StatusSession.OPENED)
											.orElseThrow(SessionNotStartedYetException::new);
	
		product.setQuantityInStore(product.getQuantityInStore() - saleDTO.getQuantityToBuy());
		
		saleRepository.save(new Sale(saleDTO.getQuantityToBuy(), saleDTO.getFixedPrice(), product, session));
		
	}
	
	
	@Transactional
	public void deleteSaleFromCheck(Long saleId) {

		Sale sale = saleRepository.findById(saleId).orElseThrow(SaleNotExistsException::new);
		
		Session session = sale.getSession();
		if(StatusSession.WAITING.equals(session.getStatusSession())) {
			session.setStatusSession(StatusSession.OPENED);
			saleRepository.delete(sale);
		}
	}

	

}
