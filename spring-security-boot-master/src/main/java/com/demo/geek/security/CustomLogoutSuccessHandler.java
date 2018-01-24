package com.demo.geek.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import com.demo.geek.utils.TokenUtil;

public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		String token  = TokenUtil.getToken(request);
		String cookie  = TokenUtil.checkCookie(request);
		TokenSecurityContextRepository.mapCache.remove(token);
		TokenSecurityContextRepository.mapCache.remove(cookie);
		super.onLogoutSuccess(request, response, authentication);
	}
}
