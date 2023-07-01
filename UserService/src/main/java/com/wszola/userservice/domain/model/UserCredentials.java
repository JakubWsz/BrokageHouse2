package com.wszola.userservice.domain.model;

public record UserCredentials(String email, String password) {

	@Override
	public String email() {
		return email;
	}

	@Override
	public String password() {
		return password;
	}
}
