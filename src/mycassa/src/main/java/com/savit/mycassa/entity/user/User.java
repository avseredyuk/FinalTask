package com.savit.mycassa.entity.user;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
	
	@ManyToMany(
			cascade=CascadeType.ALL, 
			fetch = FetchType.EAGER
	)
	@JoinTable(
			name = "users_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")			
	)
	private Set<Role> roles = new HashSet<>();

	public User(String email, String password, String firstName, String lastName, Set<Role> roles) {
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.roles = roles;
	}
	
	
	
	
}
















