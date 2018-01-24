package com.demo.geek.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demo.geek.constants.LoginConstants;
import com.demo.geek.exceptions.GenericException;

@RestController
public class UserController {

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public List<String> allUsers() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("-"+auth.getName());
		if(auth == null || auth.getName().equals(LoginConstants.ANONYMOUS)) {
			throw new GenericException("Invalid User.");
		}
		return Arrays.asList(new String[] { "A", "B" });
	}

}