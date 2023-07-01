package com.brokagehouse.authservice.rest;

import com.brokagehouse.authservice.encoder.PBKDF2Encoder;
import com.brokagehouse.authservice.model.ResponseAuth;
import com.brokagehouse.authservice.model.UserAuth;
import com.brokagehouse.authservice.model.UserCredentials;
import com.brokagehouse.authservice.service.UserService;
import com.brokagehouse.authservice.utils.JWTUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;

@RestController
@RequestMapping("/api/v1/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AuthenticationREST {
	JWTUtil jwtUtil;
	PBKDF2Encoder pbkdf2Encoder;
	UserService userService;

	@PostMapping("/login")
	public ResponseAuth login(@RequestBody UserCredentials credentials, ServerHttpResponse response) {
		UserAuth byEmail = userService.findByEmail(credentials.email());

		if (arePasswordsEqual(byEmail.getPassword(), pbkdf2Encoder.encode(Arrays.toString(credentials.password())))) {
			String token = jwtUtil.generateToken(byEmail);
			ResponseCookie cookie = ResponseCookie.from("jwt", token)
					.httpOnly(true)
					.secure(true)
					.path("/")
					.maxAge(Duration.ofHours(1))
					.sameSite("None")
					.build();
			response.addCookie(cookie);
			return new ResponseAuth(token);
		} else {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
		}
	}

	@PostMapping("/register")
	public Mono<UserAuth> register(@RequestBody UserCredentials credentials) {
		return Mono.just(userService.register(credentials.email(), Arrays.toString(credentials.password())));
	}

	@PostMapping("/logout")
	public Mono<Void> logout(ServerHttpResponse response) {
		ResponseCookie cookie = ResponseCookie.from("jwt", "")
				.maxAge(0)
				.path("/")
				.build();
		response.addCookie(cookie);
		return Mono.empty();
	}

	@PostMapping("/update-credentials")
	public void updateCredentials(@RequestBody UserCredentials userCredentials) {
		try {
			userService.updateUserCredentials(userCredentials);
		} catch (ResponseStatusException e) {
			log.error("User not found: ", e);
			throw new ResponseStatusException(e.getStatusCode(), e.getReason());
		} catch (Exception e) {
			log.error("Exception occurred during updating user data: ", e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
		}
	}

	private boolean arePasswordsEqual(String password1, String password2) {
		if (password1 == null || password2 == null) {
			return false;
		}

		return password1.chars().allMatch(Character::isWhitespace) == password2.chars().allMatch(Character::isWhitespace);
	}
}
