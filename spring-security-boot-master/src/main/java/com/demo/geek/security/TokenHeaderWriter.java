package com.demo.geek.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.header.HeaderWriter;

public class TokenHeaderWriter implements HeaderWriter {
	@Override
	public void writeHeaders(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.isAuthenticated()) {
			if (auth instanceof TokenAuthentication) {
				response.setHeader("X-API-TOKEN", ((TokenAuthentication) auth).getToken());
				response.addCookie(new Cookie("X-API-TOKEN", ((TokenAuthentication) auth).getToken()));
			}
		}
	}
}
