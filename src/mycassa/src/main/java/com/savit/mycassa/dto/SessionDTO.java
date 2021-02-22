package com.savit.mycassa.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;

import com.savit.mycassa.entity.product.Product;
import com.savit.mycassa.entity.product.Sale;
import com.savit.mycassa.entity.session.Session;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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
	
	//TODO: notEndedSession 52 line remak to session/check
}
