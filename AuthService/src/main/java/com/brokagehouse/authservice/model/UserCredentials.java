package com.brokagehouse.authservice.model;

public record UserCredentials(String email,  char[] password) {

	@Override
	public String email() {
		return email;
	}

	@Override
	public char[] password() {
		return password;
	}
}
