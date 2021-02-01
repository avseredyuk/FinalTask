package com.savit.mycassa.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

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
@Entity
@Table(
		name="users",
		uniqueConstraints={
				@UniqueConstraint(columnNames={"email"})
		}
)
public class User {
//	@SequenceGenerator(
//			name = "user_sequence",
//			sequenceName = "user_sequence",
//			allocationSize = 1
//	)
	@Id
	@GeneratedValue(
			strategy = GenerationType.IDENTITY
//			,generator = "user_sequence"
	)
	private Long id;
	
	@Column(nullable=false)
	private String email;
	
	@Column(nullable=false)
	private String password;
	
	@Column(nullable=false)
	private String firstName;
	
	@Column(nullable=false)
	private String lastName;
	
	@Enumerated(EnumType.STRING)
	private Role role;

	public User(String email, String password, String firstName, String lastName, Role role) {
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
	}
	
	
	
	
}
















