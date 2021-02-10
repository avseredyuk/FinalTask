package com.savit.mycassa.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.savit.mycassa.entity.session.Session;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="sales", uniqueConstraints= @UniqueConstraint(columnNames = {"product_id", "session_id"}))
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Sale {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private Long quantity;
	
	@Column(nullable = false)
	private Long fixedPrice;
	
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private Session session;

	public Sale(Long quantity, Long fixedPrice, Product product, Session session) {
		this.quantity = quantity;
		this.fixedPrice = fixedPrice;
		this.product = product;
		this.session = session;
	}
    
    
    
    
	
	
}
