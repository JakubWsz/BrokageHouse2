package com.wszola.userservice.infrastructure.exception;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserExistsException extends RuntimeException {

	UserExistsExceptionCode code;

	public UserExistsException(UserExistsExceptionCode code, Object... arguments) {
		super(String.format(code.getMessage(), arguments));
		this.code = code;
	}

	public UserExistsExceptionCode getCode() {
		return code;
	}

	@RequiredArgsConstructor
	@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
	public enum UserExistsExceptionCode {
		EMAIL_EXISTS("User with the provided email already exists", 400),
		PERSONAL_ID_EXISTS("User with the provided personal ID number already exists", 400);

		String message;
		int status;

		public String getMessage() {
			return message;
		}

		public int getStatus() {
			return status;
		}

		public UserExistsException createUserExistsException() {
			return new UserExistsException(this);
		}
	}
}