package com.savit.mycassa.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@ToString
public class UserData {

	private static final String REGEX = "(([^<>()\\\\.,;:\\s@\"]+(\\.[^<>()\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))";
	
	@NotBlank(message = "{sign.up}")
	@Size(max = 30)
	private  String firstName;
	
	@NotBlank
	@Size(max = 30)
	private  String lastName;
	
	@NotBlank
	@Size(max = 30)
	@Email(regexp = REGEX)
	private  String email;
	
	@NotBlank
	@Size(max = 40)
	private  String password;
	
	@NotBlank
	private String role;
	
}



