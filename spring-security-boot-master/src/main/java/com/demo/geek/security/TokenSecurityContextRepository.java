package com.demo.geek.security;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TokenSecurityContextRepository implements SecurityContextRepository {

	private ConcurrentHashMap<String, SecurityContext> mapCache = new ConcurrentHashMap<>();

	@Override
	public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
		System.out.println(mapCache);
		if (this.containsContext(requestResponseHolder.getRequest())) {
			String token = this.getToken(requestResponseHolder.getRequest());
			return (SecurityContext) mapCache.get(token);
		} else
			return SecurityContextHolder.getContext();
	}

	private String getToken(HttpServletRequest request) {
		// TODO Auto-generated method stub
		String token = request.getHeader("X-API-TOKEN");
		if (StringUtils.isEmpty(token)) {
			return checkCookie(request);
		} else {
			return token;
		}
	}

	private static String checkCookie(HttpServletRequest request) {
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals("JSESSIONID")) {
					return cookie.getValue();
				}
			}
		}

		return null;
	}

	private String checkCookieFromResponse(HttpServletResponse response) {
		if (response.getHeader("SET-COOKIE") != null) {
			String header = new String(response.getHeader("SET-COOKIE").getBytes());
			return header.substring(header.indexOf("=") + 1,
					(header.indexOf(";") != -1 ? header.indexOf(";") : header.length()));
		}
		return null;
	}

	@Override
	public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = context.getAuthentication();
		if (auth != null) {
			String token = UUID.randomUUID().toString();
			if (token != null) {
				mapCache.put(token, context);
				response.setHeader("X-API-TOKEN", token);
				response.addCookie(new Cookie("X-API-TOKEN", token));
			}
			String jsessionId = checkCookieFromResponse(response);
			if (jsessionId == null) {
				jsessionId = checkCookie(request);
			}
			if (jsessionId != null) {
				mapCache.put(jsessionId, context);
			}
		}
	}

	@Override
	public boolean containsContext(HttpServletRequest request) {
		String token = this.getToken(request);
		if (token != null) {
			return mapCache.containsKey(token);
		} else {
			return false;
		}
	}

}
