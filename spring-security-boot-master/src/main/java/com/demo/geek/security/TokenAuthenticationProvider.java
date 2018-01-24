package com.demo.geek.security;

import java.util.Collection;
import java.util.UUID;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class TokenAuthenticationProvider extends DaoAuthenticationProvider {

	private Collection<GrantedAuthority> obtainAuthorities(UserDetails user) {
		return null;
		// return granted authorities for user, according to your requirements
	}

	// return moduleCode for user, according to your requirements
	private String getToken(UserDetails user) {
		return UUID.randomUUID().toString();
	}

	public TokenAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		this.setUserDetailsService(userDetailsService);
		this.setPasswordEncoder(passwordEncoder);
	}

	@Override
	public Authentication createSuccessAuthentication(Object principal, Authentication authentication,
			UserDetails user) {
		// Suppose this user implementation has a moduleCode property
		TokenAuthentication result = new TokenAuthentication(authentication.getPrincipal(),
				authentication.getCredentials(), obtainAuthorities(user), getToken(user));
		result.setDetails(authentication.getDetails());
		return result;
	}
}