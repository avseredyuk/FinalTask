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
public class ProductDTO {

	Long id;
	
	
	@NotBlank(message = "{valid.product.title.notblank}")
	@Size(min = 1, max = 100, message = "{valid.product.title.size}")
	private String title;
	
	private String ean;
	
	@NotNull(message = "{valid.product.price.notnull}")
	@Min(value = 1, message="{valid.product.price.minmax}")
	@Max(value = 1000000, message="{valid.product.price.minmax}")
	@Positive(message = "valid.product.quantity.notnull")
	private Long price;
	
	@NotNull(message = "{valid.product.quantity.notnull}")
	@Min(value = 1, message="{valid.product.price.minmax}")
	@Max(value = 1000000, message="{valid.product.price.minmax}")
	@Positive(message = "{valid.product.quantity.positive}")
	private Long quantityInStore;
	
	
	private String measure;

}
