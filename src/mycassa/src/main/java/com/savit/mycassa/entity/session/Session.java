package com.savit.mycassa.entity.session;

import java.time.LocalDateTime;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.format.annotation.DateTimeFormat;

import com.savit.mycassa.entity.product.Sale;
import com.savit.mycassa.entity.user.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="sessions", uniqueConstraints= @UniqueConstraint(columnNames = {"user_id", "statusSession"}))
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Session {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	@DateTimeFormat(pattern = "dd.MM.yyyy HH:mm:ss")
	private LocalDateTime startedAt;
	
	@Column
	@DateTimeFormat(pattern = "dd-MM-yyyy HH-mm-ss")
	private LocalDateTime endedAt;
	
	@Enumerated(EnumType.STRING)
	private StatusSession statusSession;
	
	@OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
	private Set<Sale> sales;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

	public Session(LocalDateTime startedAt, StatusSession statusSession) {
		this.startedAt = startedAt;
		this.statusSession = statusSession;

	}
	
	
	
	
	
	
}
