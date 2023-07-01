package com.wszola.userservice.infrastructure.entity;

import com.wszola.userservice.domain.model.Address;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "BINARY(16)")
	@Setter(AccessLevel.NONE)
	UUID id;
	String firstname;
	String lastname;
	@Column(unique = true)
	String emailAddress;
	Long phoneNumber;
	@Column(unique = true)
	String personalIdNumber;
	@Embedded
	Address address;
	Boolean verified;
	boolean deleted;
	LocalDateTime modificationDate;

	public User(String firstname, String lastname, String emailAddress, Long phoneNumber, String personalIdNumber, Address address) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.emailAddress = emailAddress;
		this.phoneNumber = phoneNumber;
		this.personalIdNumber = personalIdNumber;
		this.address = address;
	}

	public User(String firstname, String lastname, String emailAddress,
				Long phoneNumber, String personalIdNumber, Address address, LocalDateTime modificationDate) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.emailAddress = emailAddress;
		this.phoneNumber = phoneNumber;
		this.personalIdNumber = personalIdNumber;
		this.address = address;
		this.modificationDate = modificationDate;
	}
}