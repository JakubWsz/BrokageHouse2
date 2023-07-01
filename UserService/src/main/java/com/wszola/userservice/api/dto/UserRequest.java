package com.wszola.userservice.api.dto;

import com.wszola.userservice.domain.model.Address;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.File;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    String firstname;
    String lastname;
    String emailAddress;
    char[] password;
    Long phoneNumber;
    String personalIdNumber;
    Address address;
    File personalIdPhoto;
}
