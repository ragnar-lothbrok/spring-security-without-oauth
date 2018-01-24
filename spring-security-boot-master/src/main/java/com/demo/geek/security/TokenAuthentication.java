package com.demo.geek.security;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class TokenAuthentication extends UsernamePasswordAuthenticationToken implements Authentication {

	private static final long serialVersionUID = 1L;

	public TokenAuthentication(Object principal, Object credentials, String token) {
		super(principal, credentials);
		this.token = token;
	}

	public TokenAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities,
			String token) {
		super(principal, credentials, authorities);
		this.token = token;
	}

	private String token;

	public String getToken() {
		return token;
	}

}