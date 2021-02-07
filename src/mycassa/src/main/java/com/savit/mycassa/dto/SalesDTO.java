package com.savit.mycassa.dto;

import java.util.List;

import com.savit.mycassa.entity.product.Sale;
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
public class SalesDTO {
	
	private String sessionStatus;

	private List<Sale> sales;
	
	private Long totalPrice;
}
