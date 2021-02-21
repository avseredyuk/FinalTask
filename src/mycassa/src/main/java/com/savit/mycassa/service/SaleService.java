package com.savit.mycassa.service;

import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.savit.mycassa.dto.SaleDTO;
import com.savit.mycassa.entity.product.Product;
import com.savit.mycassa.entity.product.Sale;
import com.savit.mycassa.entity.session.Session;
import com.savit.mycassa.entity.session.StatusSession;
import com.savit.mycassa.entity.user.User;
import com.savit.mycassa.repository.ProductRepository;
import com.savit.mycassa.repository.SaleRepository;
import com.savit.mycassa.repository.SessionRepository;
import com.savit.mycassa.repository.UserRepository;
import com.savit.mycassa.util.exception.CantPrintCheckException;
import com.savit.mycassa.util.exception.ProductNotFoundException;
import com.savit.mycassa.util.exception.SessionNotStartedYetException;
import com.savit.mycassa.util.exception.UserNotFoundException;
import com.savit.mycassa.util.pdf.CheckBuilder;

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
	
	@Autowired
	private final UserRepository userRepository;

	@Transactional
	public void newProductSaleAuth(String ean, SaleDTO saleDTO, UserDetails userDetails) {
		
		Product product = productRepository.findByEan(ean)
				.orElseThrow(ProductNotFoundException::new);
		
		Session session = sessionRepository.findByUserEmailAndByStatus(userDetails.getUsername(), StatusSession.OPENED)
											.orElseThrow(SessionNotStartedYetException::new);
	
		product.setQuantityInStore(product.getQuantityInStore() - saleDTO.getQuantityToBuy());
		
		saleRepository.save(new Sale(saleDTO.getQuantityToBuy(), saleDTO.getFixedPrice(), product, session));
		
	}
	

	public ByteArrayInputStream getProductSalesReportDocAuth(UserDetails userDetails, String ean) throws CantPrintCheckException {
		
		User user = userRepository.findByEmail(userDetails.getUsername()).get();
//		return CheckBuilder.buildProductSalesPdfReport(user, saleRepository.findByEanAndByStatusSession(ean, StatusSession.CLOSED));
		return null;//FIXME
	}


	

}
