package com.wszola.userservice.domain.service;

import com.wszola.userservice.domain.model.UserCredentials;
import com.wszola.userservice.domain.model.Address;
import com.wszola.userservice.infrastructure.entity.User;
import com.wszola.userservice.infrastructure.exception.DbException;
import com.wszola.userservice.infrastructure.exception.UserExistsException;
import com.wszola.userservice.infrastructure.external.SecurityServiceClient;
import com.wszola.userservice.infrastructure.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {
	UserRepository userRepository;
	SecurityServiceClient securityServiceClient;

	public User createUser(
			String firstname, String lastname, String emailAddress, String password,
			Long phoneNumber, String personalIdNumber, Address address
	) {
		var user = new User(firstname, lastname, emailAddress, phoneNumber, personalIdNumber,
				address);

		validateUser(user);
		securityServiceClient.registerUser(new UserCredentials(user.getEmailAddress(), password));

		return save(user);
	}

	public User updateUser(Address address, Long phoneNumber, String password, String email) {
		var fromDb = findByEmail(email);

		var updatedAddress = Optional.ofNullable(address).orElse(fromDb.getAddress());
		var updatedPhoneNumber = Optional.ofNullable(phoneNumber).orElse(fromDb.getPhoneNumber());

		if (password != null) {
			securityServiceClient.registerUser(new UserCredentials(fromDb.getEmailAddress(), password));
		}

		var toUpdate = new User(
				fromDb.getFirstname(),
				fromDb.getLastname(),
				fromDb.getEmailAddress(),
				updatedPhoneNumber,
				fromDb.getPersonalIdNumber(),
				updatedAddress,
				LocalDateTime.now()
		);

		return save(toUpdate);
	}

	public Page<User> findUsers(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	public User findByEmail(String email) {
		return findUserByEmail(email);
	}

	private void validateUser(User user) {
		if (isEmailExists(user.getEmailAddress())) {
			throw new UserExistsException(UserExistsException.UserExistsExceptionCode.EMAIL_EXISTS);
		}
		if (isPersonalIdNumberExists(user.getPersonalIdNumber())) {
			throw new UserExistsException(UserExistsException.UserExistsExceptionCode.PERSONAL_ID_EXISTS);
		}
	}

	private boolean isEmailExists(String emailAddress) {
		return userRepository.existsByEmailAddress(emailAddress);
	}

	private boolean isPersonalIdNumberExists(String personalIdNumber) {
		return userRepository.existsByPersonalIdNumber(personalIdNumber);
	}

	private User save(User user) {
		return userRepository.save(user);
	}

	private User findUserByEmail(String email) {
		return userRepository.findByEmailAddress(email)
				.orElseThrow(DbException.DbExceptionCode.NO_USER::createDbException);
	}
}