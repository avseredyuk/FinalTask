package com.savit.mycassa.dto;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;

import com.savit.mycassa.entity.product.Product;

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
public class SessionData {

	public Long id;
	
	public  LocalDateTime startedAt;
	
	public  String status;
	
	
	
}
