package com.savit.mycassa.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
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
@ToString
public class UserData {
	
	

	private static final String REGEX = "(([^<>()\\\\.,;:\\s@\"]+(\\.[^<>()\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))";
	
	@NotBlank(message = "First name cant't be empty")
	@Size(max = 40, message = "First name is too long")
	private  String firstName;
	
	@NotBlank(message = "First name cant't be empty")
	@Size(max = 40, message = "Lirst name is too long")
	private  String lastName;
	
	@NotBlank(message = "First name cant't be empty")
	@Size(max = 40, message = "Email is too long")
	@Email(regexp = REGEX,
	message = "invalid format email")
	private  String email;
	
	@NotBlank(message = "Password cant't be empty")
	@Size(max = 40, message = "Password is too long")
	private  String password;
	
//	@NotNull(message = "Choose the role")
//	private Set<Role> roles;
	
}



