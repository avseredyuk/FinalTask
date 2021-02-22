package com.savit.mycassa.dto;

import java.time.LocalDateTime;

import com.savit.mycassa.entity.session.Session;

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
public class SessionDTO {


	public Long id;
	
	public  LocalDateTime startedAt;
	
	public  String status;
	
	public static SessionDTO map(Session session) {
		return SessionDTO.builder()
				.id(session.getId())
				.startedAt(session.getStartedAt())
				.status(session.getStatusSession().name()).build();
	}
}
