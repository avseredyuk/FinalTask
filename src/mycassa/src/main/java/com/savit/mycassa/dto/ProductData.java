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
	
	Long id;
	
	
	@NotBlank(message = "{title.valid.notblank}")
	@Size(max = 100, message = "{title.valid.max}")
	private String title;
	
	private String ean;
	
	@NotNull(message = "{cost.valid.notnull}")
	@Positive
	private Long cost;
	
	@NotNull(message = "{quantityInStore.valid.notnull}")
	@Positive(message = "{quantityInStore.valid.positive}")
	private Long quantityInStore;
	
	@NotBlank(message = "{measure.valid.notblank}")
//	@Pattern(regexp = "(KILOGRAM|PIECE)", message = "{measure.valid.pattern}")
	private String measure;

}
