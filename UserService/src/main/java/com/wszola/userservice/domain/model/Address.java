package com.wszola.userservice.domain.model;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Embeddable
public class Address {
	String city;
	String postalCode;
	String streetAndNumber;
}
