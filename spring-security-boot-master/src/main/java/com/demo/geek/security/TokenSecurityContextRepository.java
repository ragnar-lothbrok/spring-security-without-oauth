package com.demo.geek.security;

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

import com.demo.geek.utils.TokenUtil;

@Service
public class TokenSecurityContextRepository implements SecurityContextRepository {

	public static ConcurrentHashMap<String, SecurityContext> mapCache = new ConcurrentHashMap<>();

	@Override
	public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
		if (this.containsContext(requestResponseHolder.getRequest())) {
			String token = TokenUtil.getToken(requestResponseHolder.getRequest());
			return (SecurityContext) mapCache.get(token);
		} else
			return SecurityContextHolder.getContext();
	}

	@Override
	public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = context.getAuthentication();
		if (auth != null && auth.isAuthenticated()) {
			if (auth instanceof TokenAuthentication) {
				mapCache.put(((TokenAuthentication) auth).getToken(), context);
				response.setHeader("X-API-TOKEN", ((TokenAuthentication) auth).getToken());
				response.addCookie(new Cookie("X-API-TOKEN", ((TokenAuthentication) auth).getToken()));
				String jsessionId = TokenUtil.checkCookieFromResponse(response);
				if (jsessionId == null) {
					jsessionId = TokenUtil.checkCookie(request);
				}
				if (jsessionId != null) {
					mapCache.put(jsessionId, context);
				}
			}
		}
	}

	@Override
	public boolean containsContext(HttpServletRequest request) {
		String token = TokenUtil.getToken(request);
		if (token != null) {
			return mapCache.containsKey(token);
		} else {
			return false;
		}
	}

}
