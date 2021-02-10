package com.savit.mycassa.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TimeBordersDTO {
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	//	@Past(message = "time.must.be.in.past")
	LocalDateTime timeFrom;
	
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	//	@Past(message = "time.must.be.in.past")
	LocalDateTime timeTo;

}
