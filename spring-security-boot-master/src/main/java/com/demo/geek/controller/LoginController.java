package com.demo.geek.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.demo.geek.domain.LoginCred;
import com.demo.geek.utils.TokenUtil;

@RestController
public class LoginController {

	final static Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private RestTemplate restTemplate;

	@Value("${spring.url.login}")
	private String loginUrl;

	@Value("${spring.url.logout}")
	private String logoutUrl;

	@RequestMapping(value = "/userlogin", method = RequestMethod.POST)
	public String userlogin(HttpServletRequest request, HttpServletResponse response,
			@RequestBody LoginCred userCredential) {
		logger.info("loginCred received = {} ", userCredential);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.add("Cookie", "JSESSIONID=" + TokenUtil.checkCookie(request));

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("username", userCredential.getUsername());
		map.add("password", userCredential.getPassword());

		HttpEntity<MultiValueMap<String, String>> loginRequest = new HttpEntity<MultiValueMap<String, String>>(map,
				httpHeaders);

		ResponseEntity<String> loginResponse = restTemplate.postForEntity(loginUrl, loginRequest, String.class);
		if (loginResponse.getHeaders().get("Location") != null
				&& loginResponse.getHeaders().get("Location").size() > 0) {
			String redirectUri = loginResponse.getHeaders().get("Location").get(0);
			if (redirectUri.indexOf("login") == -1) {
				httpHeaders = new HttpHeaders();
				httpHeaders.add("Cookie", "JSESSIONID=" + TokenUtil.checkCookie(request));
				loginResponse = restTemplate.exchange(redirectUri, HttpMethod.GET, new HttpEntity<>(httpHeaders),
						String.class);
			} else {
				throw new BadCredentialsException("Invalid credentials.");
			}
		}
		String tokenResponse = loginResponse.getBody();
		return tokenResponse;
	}

	@RequestMapping(value = "/userlogout", method = RequestMethod.GET)
	public String userLogout(HttpServletRequest request, HttpServletResponse response) {
		logger.info("logout request received.");

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("X-API-TOKEN", TokenUtil.getToken(request));
		httpHeaders.add("Cookie", "JSESSIONID=" + TokenUtil.checkCookie(request));

		ResponseEntity<String> loginResponse = restTemplate.exchange(logoutUrl, HttpMethod.GET,
				new HttpEntity<>(httpHeaders), String.class);

		return loginResponse.getBody();
	}
}
