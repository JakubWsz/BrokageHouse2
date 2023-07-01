package com.wszola.userservice.infrastructure.repository;

import com.wszola.userservice.infrastructure.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
	Optional<User> findByEmailAddress(String emailAddress);

	Page<User> findAll(Pageable pageable);

	boolean existsByEmailAddress(String name);

	boolean existsByPersonalIdNumber(String name);
}
