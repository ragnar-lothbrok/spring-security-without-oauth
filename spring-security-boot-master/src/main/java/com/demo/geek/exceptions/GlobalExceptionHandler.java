package com.demo.geek.exceptions;

import javax.annotation.PostConstruct;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

	@PostConstruct
	public void test() {
		System.out.println();
	}

	@ExceptionHandler(BadCredentialsException.class)
	@ResponseBody
	public ResponseEntity<ApiError> process1(BadCredentialsException ex) {
		return new ResponseEntity<ApiError>(new ApiError(ex.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(GenericException.class)
	@ResponseBody
	public ResponseEntity<ApiError> process2(GenericException ex) {
		return new ResponseEntity<ApiError>(new ApiError(ex.getMessage()), HttpStatus.BAD_REQUEST);
	}

	private static class ApiError {
		private String errorMessage;

		public ApiError(String errorMessage) {
			super();
			this.errorMessage = errorMessage;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}

	}
}
