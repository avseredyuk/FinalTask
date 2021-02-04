package com.savit.mycassa.entity.user.details;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.savit.mycassa.entity.user.Role;
import com.savit.mycassa.entity.user.User;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor

public class UserDetailsImpl implements UserDetails {

	private User user;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());
		return Collections.singletonList(authority);
	}
	
	public Long getId() {
		return user.getId();
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public String getFirstName() {
		return this.user.getFirstName();
	}
	
	public String getLastName() {
		return this.user.getLastName();
	}
	
	public Role getRole() {
		return this.user.getRole();
	}
	
	
	
	public String toString() {
		return String.format("User:{email:%s,password:%s,firstName:%s,lastName:%s,role:%s}", user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getRole().name());
	}

}
