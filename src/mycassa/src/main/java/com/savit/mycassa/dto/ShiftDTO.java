package com.savit.mycassa.dto;

import java.time.LocalDateTime;

import com.savit.mycassa.entity.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ShiftDTO {


	public Long id;
	
	public  LocalDateTime startedAt;
	
	public  LocalDateTime endedAt;
	
	public  String status;	
}
