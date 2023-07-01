package com.brokagehouse.authservice.service;

import com.brokagehouse.authservice.db.UserAuthJPARepository;
import com.brokagehouse.authservice.encoder.PBKDF2Encoder;
import com.brokagehouse.authservice.model.Role;
import com.brokagehouse.authservice.model.UserAuth;
import com.brokagehouse.authservice.model.UserCredentials;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {
	PBKDF2Encoder pbkdf2Encoder;
	UserAuthJPARepository userAuthJPARepository;

	public UserAuth findByEmail(String email) {
		return userAuthJPARepository.findByUsername(email)
				.orElseThrow();
	}

	public UserAuth register(String username, String password) {
//		String hashPassword = pbkdf2Encoder.encode(rawPassword);
		return userAuthJPARepository.save(new UserAuth(username, password,
				true, List.of(Role.ROLE_REGULAR)));
	}

	public void updateUserCredentials(UserCredentials userCredentials) throws ResponseStatusException {
		log.info("Updating user credentials for {}", userCredentials.email());
		var userAuth = userAuthJPARepository.findByUsername(userCredentials.email())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

		var toUpdate = new UserAuth(
				userAuth.getId(), userAuth.getUsername(), Arrays.toString(userCredentials.password()),
				userAuth.isEnabled(), userAuth.getRoles()
		);
		userAuthJPARepository.save(toUpdate);
	}
}