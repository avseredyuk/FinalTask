package com.savit.mycassa.entity.shift;

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

import com.savit.mycassa.entity.session.Session;
import com.savit.mycassa.entity.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="shifts")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter
public class Shift {

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
	private StatusShift statusShift;
	
	@OneToMany(mappedBy = "shift", cascade = CascadeType.ALL)
	private List<Session> sessions;
	

	public Shift(LocalDateTime startedAt, StatusShift statusShift) {
		this.startedAt = startedAt;
		this.statusShift = statusShift;
	}
	
	
	
	
	
	
}
