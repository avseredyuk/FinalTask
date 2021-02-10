package com.savit.mycassa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.savit.mycassa.entity.product.Sale;
import com.savit.mycassa.entity.session.StatusSession;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

	
//	
	@Query("SELECT sale FROM Sale AS sale "
			+ "INNER JOIN FETCH sale.session "
				+ "WHERE sale.session.id = ?1")

	List<Sale> findAllBySessionId(Long session_id);

	
	@Query("SELECT sale FROM Sale sale "
			+ "INNER JOIN FETCH sale.session session "
			+ "INNER JOIN FETCH sale.product product "
			+ "WHERE product.ean = ?1 AND session.statusSession = ?2")
	List<Sale>  findByEanAndByStatusSession(String ean, StatusSession closed);

//	@Query("UPDATE")
//	void deleteAllBySessionId(Long session_id);

}
