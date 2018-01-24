package com.demo.geek.repo;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Component;

import com.demo.geek.domain.User;

@Component
public class UserRepository {

	public static List<User> validUsers = new ArrayList<>();

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostConstruct
	public void addUserToPool() {
		validUsers.add(new User("admin", ((StandardPasswordEncoder) passwordEncoder).encode("admin"), "ROLE_ADMIN"));
		validUsers.add(new User("user", ((StandardPasswordEncoder) passwordEncoder).encode("user"), "ROLE_USER"));
	}

}
