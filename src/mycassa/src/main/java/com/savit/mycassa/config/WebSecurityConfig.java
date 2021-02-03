package com.savit.mycassa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.savit.mycassa.service.UserService;

import lombok.AllArgsConstructor;


@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	
	@Autowired
	private final UserService userService;
	
	@Autowired
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.authorizeRequests()
		.antMatchers("/css/**","/js/**","/images/**").permitAll()
		.antMatchers("/registration", "/welcome", "/login").permitAll()
		.antMatchers("/profile/**").hasAnyAuthority("CASHIER", "COMMODITY_EXPERT", "SENIOR_CASHIER")
		.antMatchers("/products/sale").hasAnyAuthority("CASHIER")
		.antMatchers("/products/new").hasAnyAuthority("COMMODITY_EXPERT")
		.antMatchers("/session/**").hasAnyAuthority("CASHIER")
		.antMatchers("/statistics/**", "/cashiers/**").hasAuthority("SENIOR_CASHIER")
		
		.anyRequest().authenticated()
//			.and()
//		.exceptionHandling().accessDeniedPage("/unauthorized")
			.and()
		.formLogin().permitAll()
		.loginPage("/login")
        .loginProcessingUrl("/perform-login")
        .defaultSuccessUrl("/profile",true)
			.and()
		.logout().permitAll()
		.logoutSuccessUrl("/welcome");
	}
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(daoAuthenticationProvider());
		super.configure(auth);
	}
	
	@Bean 
	DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(bCryptPasswordEncoder);
		provider.setUserDetailsService(userService);
		return provider;
	}
	
	

	
	
}
