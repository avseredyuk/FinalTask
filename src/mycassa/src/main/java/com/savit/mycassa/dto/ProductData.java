package com.savit.mycassa.dto;


import javax.validation.constraints.*;
import com.savit.mycassa.entity.product.Measure;

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
@EqualsAndHashCode
@Builder
@ToString
public class ProductData {
	
	
	@NotBlank
	@Size(max = 40)
	private String title;
	
	private String ean;
	
	@NotBlank
	@Positive
	private Long cost;
	
	@NotBlank
	@Positive
	private Long quantityInStore;
	
	@NotBlank
	@Pattern(regexp = "(KILOGRAM|PIECE)")
	private String measure;

}
