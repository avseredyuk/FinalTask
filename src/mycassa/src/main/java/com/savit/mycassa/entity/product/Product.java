package com.savit.mycassa.entity.product;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Check;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(
		name="products",
		uniqueConstraints={
				@UniqueConstraint(columnNames={"ean"})
		}
)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
@Check(constraints = "quantity_in_store >= 0")
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String ean;
	
	@Column(nullable=false)
	private String title;
	
	@Column(nullable = false)
	private Long price;
	

	@Column(nullable = false)
	private Long quantityInStore;
	
	@Enumerated(EnumType.STRING)
	private Measure measure;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Sale> sales;

	public Product(String title, Long price, Long quantityInStore, Measure measure) {
		this.title = title;
		this.price = price;
		this.quantityInStore = quantityInStore;
		this.measure = measure;
	}

	public Product(String ean, String title, Long price, Long quantityInStore, Measure measure, Set<Sale> sales) {
		this.ean = ean;
		this.title = title;
		this.price = price;
		this.quantityInStore = quantityInStore;
		this.measure = measure;
		this.sales = sales;
	}
	
	
}
