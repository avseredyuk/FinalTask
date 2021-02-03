package com.savit.mycassa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.savit.mycassa.entity.session.Session;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long>  {
	
	
	
	

}
