package com.savit.mycassa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


//@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@SpringBootApplication
public class MycassaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MycassaApplication.class, args);
//		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//		String encodedPassword = bCryptPasswordEncoder
//				.encode("dsa");
//		System.out.println(encodedPassword);
	}

}
