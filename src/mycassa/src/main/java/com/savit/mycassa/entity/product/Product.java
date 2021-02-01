package com.savit.mycassa.entity.product;

import java.util.Set;

import javax.persistence.*;

import lombok.AllArgsConstructor;
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
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String ean;
	
	@Column(nullable=false)
	private String title;
	
	@Column(nullable = false)
	private Long cost;
	
	@Column(nullable = false)
	private Long quantityInStore;
	
	@Enumerated(EnumType.STRING)
	private Measure measure;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private Set<Sale> sales;

	public Product(String title, Long cost, Long quantityInStore, Measure measure) {
		this.title = title;
		this.cost = cost;
		this.quantityInStore = quantityInStore;
		this.measure = measure;
	}

	public Product(String ean, String title, Long cost, Long quantityInStore, Measure measure, Set<Sale> sales) {
		this.ean = ean;
		this.title = title;
		this.cost = cost;
		this.quantityInStore = quantityInStore;
		this.measure = measure;
		this.sales = sales;
	}
	
	
}
