package com.savit.mycassa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.savit.mycassa.entity.session.Session;
import com.savit.mycassa.entity.session.StatusSession;
import com.savit.mycassa.entity.user.User;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long>  {
	

	@Query("SELECT s FROM Session AS s INNER JOIN FETCH s.user WHERE s.user.id = ?1 AND s.statusSession = ?2")
	List<Session> findByUserAndByStatusSession(Long user_id, StatusSession statusSession);

}
