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
	
	@NotBlank(message = "{valid.user.firstName.notblank}")
	@Size(max = 30, message = "{valid.user.firstName.max}")
	private  String firstName;
	
	@NotBlank(message = "{valid.user.lastName.notblank}")
	@Size(max = 30, message = "{valid.user.lastName.max}")
	private  String lastName;
	
	@NotBlank(message = "{valid.user.email.notblank}")
	@Size(max = 30, message = "{valid.user.email.max}")
	@Email(regexp = REGEX, message = "{valid.user.email.regex}")
	private  String email;
	
	@NotBlank(message = "{valid.user.password.notblank}")
	@Size(max = 30, message = "{valid.user.password.max}")
	private  String password;
	
	private String role;
	
}



