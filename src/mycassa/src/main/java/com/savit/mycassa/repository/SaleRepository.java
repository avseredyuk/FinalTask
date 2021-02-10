package com.savit.mycassa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.savit.mycassa.entity.product.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

	
//	 "FROM Mark AS markk "
//				+ "INNER JOIN FETCH markk.student  "
//				+ "INNER JOIN FETCH markk.student.class_  "
//				+ "INNER JOIN FETCH markk.subject  "
//				+ "WHERE markk.mark >= :min_mark AND markk.mark < :max_mark "
//	
	@Query("SELECT sale FROM Sale AS sale "
			+ "INNER JOIN FETCH sale.session "
				+ "WHERE sale.session.id = ?1")

	List<Sale> findAllBySessionId(Long session_id);

//	@Query("UPDATE")
//	void deleteAllBySessionId(Long session_id);

}
