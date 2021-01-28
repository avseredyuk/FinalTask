package com.savit.mycassa.config;

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

	
	private final UserService userService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests()
					.antMatchers("/welcome").hasAnyAuthority("USER", "CASHIER", "SENIOR_CASHIER", "COMMODITY_EXPERT")
					.antMatchers("/products").hasAnyAuthority("CASHIER", "SENIOR_CASHIER", "COMMODITY_EXPERT")
					.antMatchers("/admin", "/newProduct").hasAnyAuthority("COMMODITY_EXPERT")
					.antMatchers("/cassier/**", "/newSell").hasAnyAuthority("CASHIER")
					.antMatchers("/senior-cassier", "cashierlist").hasAnyAuthority("SENIOR_CASHIER")
					.antMatchers("/registration/")
					.permitAll()
					.anyRequest().authenticated()
				.and()
					.exceptionHandling().accessDeniedPage("/unauthorized")
				.and()
					.formLogin()
					.loginPage("/login")
					.defaultSuccessUrl("/welcome")
					.failureUrl("/login?error")
					.permitAll()
				.and()
					.logout()
					.permitAll()
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
