package com.savit.mycassa.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.savit.mycassa.entity.product.Sale;

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
public class CheckDTO {

	public String sessionId;
	
	public  LocalDateTime startedAt;
	
	public  String status;
	
	private List<Sale> sales;
	
	private Long totalPrice;
}
