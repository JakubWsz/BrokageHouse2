package com.wszola.userservice.api.dto;

import com.wszola.userservice.domain.model.Address;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserRequest {
    char[] password;
    Long phoneNumber;
    Address address;
}
