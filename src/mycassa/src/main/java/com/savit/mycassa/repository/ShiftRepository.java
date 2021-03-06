package com.savit.mycassa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.savit.mycassa.entity.shift.Shift;
import com.savit.mycassa.entity.shift.StatusShift;

public interface ShiftRepository extends JpaRepository<Shift, Long> {

	
	
	Optional<Shift>  findByStatusShift(StatusShift statusShift);

	Optional<Shift> findByIdAndStatusShift(Long shift_id, StatusShift status);

	
	
	
	
	

}
