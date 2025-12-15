package org.springframework.petmanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.lang.Nullable;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.rest.dto.AdminUserUpdateDto;
import org.springframework.petmanagement.rest.dto.UserRegistrationDto;

public interface UserService {
    User createUser(UserRegistrationDto fields);
    User updateUser(UUID userId, AdminUserUpdateDto fields);
    void deleteUser(UUID userId);

    Optional<User> getUser(UUID userId);
    List<User> listUsersByName(@Nullable String lastNameKana, @Nullable String firstNameKana);
}
