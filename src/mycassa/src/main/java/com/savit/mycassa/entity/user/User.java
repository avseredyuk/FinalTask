package com.savit.mycassa.entity.user;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


import com.savit.mycassa.entity.session.Session;
import com.savit.mycassa.entity.user.details.UserDetailsImpl;

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

	@Id
	@GeneratedValue(
			strategy = GenerationType.IDENTITY
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

	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Session> sessions;
	
	
	public User(String email, String password, String firstName, String lastName, Role role) {
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
	}
	
	public static User buildUser(UserDetailsImpl ud) {
		return User.builder()
							.id(ud.getId())
							.email(ud.getUsername())
							.password(ud.getPassword())
							.firstName(ud.getFirstName())
							.lastName(ud.getLastName())
							.role(ud.getRole()).build();
	}
	
}
















