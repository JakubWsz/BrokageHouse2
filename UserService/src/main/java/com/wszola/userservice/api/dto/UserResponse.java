package com.wszola.userservice.api.dto;

import com.wszola.userservice.domain.model.Address;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String firstname;
    String lastname;
    String emailAddress;
    Long phoneNumber;
    String personalIdNumber;
    Address address;
}
