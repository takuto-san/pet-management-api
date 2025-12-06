package org.springframework.petmanagement.rest.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.petmanagement.mapper.PetMapper;
import org.springframework.petmanagement.mapper.UserMapper;
import org.springframework.petmanagement.model.Pet;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.rest.api.UsersApi;
import org.springframework.petmanagement.rest.dto.PetDto;
import org.springframework.petmanagement.rest.dto.PetFieldsDto;
import org.springframework.petmanagement.rest.dto.UserDto;
import org.springframework.petmanagement.rest.dto.UserFieldsDto;
import org.springframework.petmanagement.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/api")
public class UserRestController implements UsersApi {

    private final UserService userService;
    private final UserMapper userMapper;
    private final PetMapper petMapper;

    public UserRestController(UserService userService,
                              UserMapper userMapper,
                              PetMapper petMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.petMapper = petMapper;
    }

    @PreAuthorize("hasRole(@roles.ADMIN)")
    @Override
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody UserFieldsDto userFieldsDto) {
        User created = userService.createUser(userFieldsDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(
            UriComponentsBuilder.newInstance()
                .path("/api/users/{id}")
                .buildAndExpand(created.getId())
                .toUri()
        );
        return new ResponseEntity<>(userMapper.toUserDto(created), headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole(@roles.ADMIN)")
    @Override
    public ResponseEntity<UserDto> updateUser(UUID userId, @Valid @RequestBody UserFieldsDto userFieldsDto) {
        try {
            User updated = userService.updateUser(userId, userFieldsDto);
            return new ResponseEntity<>(userMapper.toUserDto(updated), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            if (ex.getMessage() != null && ex.getMessage().contains("not found")) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole(@roles.ADMIN)")
    @Override
    public ResponseEntity<Void> deleteUser(UUID userId) {
        try {
            userService.deleteUser(userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole(@roles.ADMIN, @roles.OWNER_ADMIN)")
    @Override
    public ResponseEntity<UserDto> getUser(UUID userId) {
        Optional<User> userOpt = userService.findById(userId);
        return userOpt
            .map(user -> new ResponseEntity<>(userMapper.toUserDto(user), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasAnyRole(@roles.ADMIN, @roles.OWNER_ADMIN)")
    @Override
    public ResponseEntity<List<UserDto>> listUsers(String lastNameKana, String firstNameKana) {
        List<User> users = userService.search(lastNameKana, firstNameKana);
        List<UserDto> dtoList = users.stream()
            .map(userMapper::toUserDto)
            .collect(Collectors.toList());
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole(@roles.ADMIN, @roles.OWNER_ADMIN)")
    @Override
    public ResponseEntity<PetDto> getUsersPet(UUID userId, UUID petId) {
        Optional<Pet> petOpt = userService.findUsersPet(userId, petId);
        return petOpt
            .map(p -> new ResponseEntity<>(petMapper.toPetDto(p), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasRole(@roles.ADMIN)")
    @Override
    public ResponseEntity<PetDto> addPetToUser(UUID userId, @Valid @RequestBody PetFieldsDto petFieldsDto) {
        try {
            Pet pet = userService.addPetToUser(userId, petFieldsDto);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(
                UriComponentsBuilder.newInstance()
                    .path("/api/users/{userId}/pets/{petId}")
                    .buildAndExpand(userId, pet.getId())
                    .toUri()
            );
            return new ResponseEntity<>(petMapper.toPetDto(pet), headers, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage() == null ? "" : ex.getMessage();
            if (msg.contains("user not found")) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else if (msg.contains("pet type not found")) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole(@roles.ADMIN)")
    @Override
    public ResponseEntity<PetDto> updateUsersPet(UUID userId, UUID petId,
                                                 @Valid @RequestBody PetFieldsDto petFieldsDto) {
        try {
            Pet pet = userService.updateUsersPet(userId, petId, petFieldsDto);
            return new ResponseEntity<>(petMapper.toPetDto(pet), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage() == null ? "" : ex.getMessage();
            if (msg.contains("user not found") || msg.contains("pet not found")) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else if (msg.contains("pet type not found")) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}