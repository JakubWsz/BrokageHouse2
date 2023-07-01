package com.wszola.userservice.infrastructure.exception;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

public class DbException extends RuntimeException {

	private final DbExceptionCode code;

	public DbException(DbExceptionCode code, Object... arguments) {
		super(String.format(code.getMessage(), arguments));
		this.code = code;
	}

	public DbExceptionCode getCode() {
		return code;
	}

	@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
	@RequiredArgsConstructor
	public enum DbExceptionCode {
		NO_USER("There is no such user with passed email", 400);

		String message;
		int status;

		public String getMessage() {
			return message;
		}

		public int getStatus() {
			return status;
		}

		public DbException createDbException() {
			return new DbException(this);
		}
	}
}