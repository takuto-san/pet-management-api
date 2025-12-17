package org.springframework.petmanagement.rest.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.petmanagement.mapper.UserMapper;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.rest.api.UsersApi;
import org.springframework.petmanagement.rest.dto.UserBaseDto;
import org.springframework.petmanagement.rest.dto.UserPageDto;
import org.springframework.petmanagement.rest.dto.UserRegistrationDto;
import org.springframework.petmanagement.rest.dto.UserResponseDto;
import org.springframework.petmanagement.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/api")
public class UserController implements UsersApi {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService,
                              UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PreAuthorize("hasRole(@roles.ADMIN)")
    @Override
    public ResponseEntity<UserResponseDto> addUser(@Valid UserRegistrationDto userRegistrationDto) {
        User created = userService.createUser(userRegistrationDto);
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
    public ResponseEntity<UserResponseDto> updateUser(UUID userId, UserBaseDto body) {
        try {
            User updated = userService.updateUserBase(userId, body);
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

    @PreAuthorize("hasAnyRole(@roles.ADMIN, @roles.CLINIC_ADMIN)")
    @Override
    public ResponseEntity<UserResponseDto> getUser(UUID userId) {
        try {
            User user = userService.getUser(userId);
            return new ResponseEntity<>(userMapper.toUserDto(user), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole(@roles.ADMIN, @roles.CLINIC_ADMIN)")
    @Override
    public ResponseEntity<UserPageDto> listUsers(Integer page, Integer size, String lastNameKana, String firstNameKana) {
        List<User> users = userService.listUsersByName(lastNameKana, firstNameKana);
        List<UserResponseDto> dtoList = users.stream()
            .map(userMapper::toUserDto)
            .collect(Collectors.toList());
        UserPageDto pageDto = new UserPageDto();
        pageDto.setContent(dtoList);
        pageDto.setSize(size);
        pageDto.setNumber(page);
        pageDto.setTotalElements(dtoList.size());
        pageDto.setTotalPages(1); // TODO: ページネーション実装
        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }
}
