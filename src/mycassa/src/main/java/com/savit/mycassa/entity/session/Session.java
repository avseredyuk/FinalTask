package com.savit.mycassa.entity.session;

import java.time.LocalDateTime;
import java.util.List;

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

import org.springframework.format.annotation.DateTimeFormat;

import com.savit.mycassa.entity.product.Sale;
import com.savit.mycassa.entity.shift.Shift;
import com.savit.mycassa.entity.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="sessions")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
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
	
//	@OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.EAGER)

	@OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
private List<Sale> sales;
	
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "shift_id")
    private Shift shift;

	public Session(LocalDateTime startedAt, StatusSession statusSession, Shift shift) {
		this.startedAt = startedAt;
		this.statusSession = statusSession;
		this.shift = shift;
	}
	
	
	
	
	
	
}
