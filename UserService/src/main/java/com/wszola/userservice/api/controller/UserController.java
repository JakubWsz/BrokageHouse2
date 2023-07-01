package com.wszola.userservice.api.controller;

import com.wszola.userservice.api.dto.UpdateUserRequest;
import com.wszola.userservice.api.dto.UserRequest;
import com.wszola.userservice.api.dto.UserResponse;
import com.wszola.userservice.domain.service.UserService;
import com.wszola.userservice.infrastructure.encoder.PBKDF2Encoder;
import com.wszola.userservice.infrastructure.entity.User;
import com.wszola.userservice.infrastructure.mapper.DtoMapper;
import com.wszola.userservice.infrastructure.utils.CustomOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
	UserService userService;
	PBKDF2Encoder pbkdf2Encoder;
	DtoMapper dtoMapper;

	@PostMapping
	@CustomOperation(summary = "Create a new user")
	public UserResponse createUser(@RequestBody UserRequest userRequest) {
		User user = userService.createUser(
				userRequest.getFirstname(),
				userRequest.getLastname(),
				userRequest.getEmailAddress(),
				pbkdf2Encoder.encode(Arrays.toString(userRequest.getPassword())),
				userRequest.getPhoneNumber(),
				userRequest.getPersonalIdNumber(),
				userRequest.getAddress()
		);
		log.info("Created user with email address '{}'", user.getEmailAddress());
		return dtoMapper.toUserResponse(user);
	}

	@PatchMapping("/update-details")
	@CustomOperation(summary = "Update an existing user's details")
	public UserResponse updateUserDetails(
			@RequestBody UpdateUserRequest userRequest, @RequestParam String userEmail) {
		User user = userService.updateUser(
				userRequest.getAddress(),
				userRequest.getPhoneNumber(),
				pbkdf2Encoder.encode(Arrays.toString(userRequest.getPassword())),
				userEmail
		);
		log.info("Updated user with email address '{}'", user.getEmailAddress());
		return dtoMapper.toUserResponse(user);
	}

	@GetMapping
	@CustomOperation(summary = "Get a list of users")
	public Page<UserResponse> findUsers(@PageableDefault(size = 10) Pageable pageable) {
		log.info("Retrieving users");
		return userService.findUsers(pageable)
				.map(dtoMapper::toUserResponse);
	}

	@GetMapping("/{userEmail}")
	@CustomOperation(summary = "Get a user by email address")
	public UserResponse findUserByEmail(@PathVariable String userEmail) {
		log.info("Retrieving user with email: '{}'", userEmail);
		return dtoMapper.toUserResponse(userService.findByEmail(userEmail));
	}
}