package com.savit.mycassa.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.savit.mycassa.entity.session.Session;
import com.savit.mycassa.entity.session.StatusSession;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long>  {
	
	
	@Query("SELECT s FROM Session AS s INNER JOIN FETCH s.user WHERE s.user.email = ?1 AND s.endedAt IS NULL")
	Optional<Session> findByUserEmailAndNotEnded(String email);

	@Query("SELECT s FROM Session AS s INNER JOIN FETCH s.user WHERE s.user.email = ?1 AND s.statusSession = ?2")
	Optional<Session> findByUserEmailAndByStatus(String email, StatusSession statusSession);
	
	@Query("SELECT s FROM Session AS s WHERE s.id = ?1 AND s.statusSession = ?2")
	Optional<Session> findByIdAndByStatusSession(Long sessionId, StatusSession waiting);

	
	@Query("SELECT COUNT(s) FROM Session AS s INNER JOIN s.user WHERE s.user.email = ?1 AND s.endedAt IS NULL")
	Integer findCountByUserEmailAndNotEnded(String email);
	
	
	@Query("SELECT COUNT(s) FROM Session AS s INNER JOIN s.shift WHERE s.shift.id = ?1 AND s.endedAt IS NULL")
	Integer findCountByShiftIdAndNotEnded(Long shiftId);
	
	
	
	@Query("SELECT s FROM Session AS s WHERE s.statusSession = ?1")
	List<Session> findByStatusSession(StatusSession statusSession);
	
	@Query("SELECT s FROM Session AS s WHERE s.statusSession = ?1 AND s.startedAt >= ?2 AND s.endedAt <=?3")
	List<Session> findByStatusSessionAndByTimeBorders(StatusSession statusSession, LocalDateTime startedAt, LocalDateTime endedAt);


}
