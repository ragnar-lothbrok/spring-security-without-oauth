package com.demo.geek.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;

import com.demo.geek.security.CustomUserDetailsService;
import com.demo.geek.security.TokenAuthenticationProvider;
import com.demo.geek.security.TokenHeaderWriter;
import com.demo.geek.security.TokenSecurityContextRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	// This Password Encoder is used to encode plain text code in secure one
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new StandardPasswordEncoder("secret");
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(tokenAuthenticationProvider());
	}

	@Bean
	public AuthenticationProvider tokenAuthenticationProvider() {
		return new TokenAuthenticationProvider(userDetailsService(),passwordEncoder());
	}

	@Bean
	@Override
	protected UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.exceptionHandling().and().formLogin().loginProcessingUrl("/login").permitAll().and().logout()
				.logoutUrl("/logout").deleteCookies("JSESSIONID").permitAll().and().csrf().disable().securityContext()
				.securityContextRepository(tokenSecurityContextRepository());

		http.headers().addHeaderWriter(new TokenHeaderWriter()).and().authorizeRequests().antMatchers("/user")
				.authenticated();
	}

	@Bean
	public SecurityContextRepository tokenSecurityContextRepository() {
		return new TokenSecurityContextRepository();
	}

}
