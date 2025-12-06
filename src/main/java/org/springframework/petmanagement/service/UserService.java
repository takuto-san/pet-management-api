package org.springframework.petmanagement.service;

import org.springframework.lang.Nullable;
import org.springframework.petmanagement.model.Pet;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.rest.dto.PetFieldsDto;
import org.springframework.petmanagement.rest.dto.UserFieldsDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User createUser(UserFieldsDto fields);
    User updateUser(UUID userId, UserFieldsDto fields);
    void deleteUser(UUID userId);

    Optional<User> findById(UUID userId);
    List<User> search(@Nullable String lastNameKana, @Nullable String firstNameKana);

    Pet addPetToUser(UUID userId, PetFieldsDto fields);
    Pet updateUsersPet(UUID userId, UUID petId, PetFieldsDto fields);
    Optional<Pet> findUsersPet(UUID userId, UUID petId);
}