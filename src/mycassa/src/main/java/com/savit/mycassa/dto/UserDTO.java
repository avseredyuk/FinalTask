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
public class UserDTO {
//FIXME incorrect localization on  rus
	private static final String REGEX = "(([^<>()\\\\.,;:\\s@\"]+(\\.[^<>()\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))";
	
	@NotBlank(message = "{firstName.valid.notblank}")
	@Size(max = 30, message = "{firstName.valid.max}")
	private  String firstName;
	
	@NotBlank(message = "{lastName.valid.notblank}")
	@Size(max = 30, message = "{lastName.valid.max}")
	private  String lastName;
	
	@NotBlank(message = "{email.valid.notblank}")
	@Size(max = 30, message = "{email.valid.max}")
	@Email(regexp = REGEX, message = "{email.valid.email}")
	private  String email;
	
	@NotBlank(message = "{password.valid.notblank}")
	@Size(max = 30, message = "{password.valid.max}")
	private  String password;
	
	@NotBlank(message = "{role.valid.notblank}")
	private String role;
	
}



