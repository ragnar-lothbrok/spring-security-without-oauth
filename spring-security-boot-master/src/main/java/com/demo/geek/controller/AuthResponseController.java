package com.demo.geek.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AuthResponseController {

	@RequestMapping(method = RequestMethod.GET)
	public Authentication getAuthDetails() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth;
	}

}
