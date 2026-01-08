package org.springframework.petmanagement.rest.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import org.springframework.petmanagement.rest.dto.PetPageDto;
import org.springframework.petmanagement.rest.dto.UserBaseDto;
import org.springframework.petmanagement.rest.dto.UserPageDto;
import org.springframework.petmanagement.rest.dto.UserRegistrationDto;
import org.springframework.petmanagement.rest.dto.UserResponseDto;
import org.springframework.petmanagement.service.PetService;
import org.springframework.petmanagement.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
public class UserController implements UsersApi {

    private final UserService userService;
    private final UserMapper userMapper;
    private final PetService petService;
    private final PetMapper petMapper;

    public UserController(UserService userService,
                              UserMapper userMapper,
                              PetService petService,
                              PetMapper petMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.petService = petService;
        this.petMapper = petMapper;
    }

    @PreAuthorize("hasRole(@roles.ADMIN)")
    @Override
    public ResponseEntity<UserResponseDto> addUser(@Valid UserRegistrationDto userRegistrationDto) {
        User created = userService.createUser(userRegistrationDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(
            UriComponentsBuilder.newInstance()
                .path("/users/{id}")
                .buildAndExpand(created.getId())
                .toUri()
        );
        return new ResponseEntity<>(userMapper.toUserDto(created), headers, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<UserResponseDto> updateUser(UUID userId, UserBaseDto body) {
        UUID currentUserId = getCurrentUserId();
        if (!userId.equals(currentUserId) && !hasRole("ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
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

    @Override
    public ResponseEntity<Void> deleteUser(UUID userId) {
        UUID currentUserId = getCurrentUserId();
        if (!userId.equals(currentUserId) && !hasRole("ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            userService.deleteUser(userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<UserResponseDto> getUser(UUID userId) {
        UUID currentUserId = getCurrentUserId();
        if (!userId.equals(currentUserId) && !hasRole("ADMIN") && !hasRole("CLINIC_ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
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

    @Override
    public ResponseEntity<PetPageDto> listPetsByUser(UUID userId, Integer page, Integer size) {
        UUID currentUserId = getCurrentUserId();
        if (!userId.equals(currentUserId) && !hasRole("ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            // Check if user exists
            userService.getUser(userId);
            Pageable pageable = PageRequest.of(page, size);
            var pets = petService.listPetsByUser(userId, pageable);
            return ResponseEntity.ok(petMapper.toPetPageDto(pets));
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof org.springframework.security.oauth2.jwt.Jwt jwt) {
            String userIdStr = jwt.getClaim("userId");
            return UUID.fromString(userIdStr);
        } else if (authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User user) {
            String userIdStr = user.getUsername();
            return UUID.fromString(userIdStr);
        }
        throw new RuntimeException("Unable to get user ID from authentication");
    }

    @Override
    public ResponseEntity<PetDto> createPet(UUID userId, PetFieldsDto petFieldsDto) {
        UUID currentUserId = getCurrentUserId();
        if (!userId.equals(currentUserId) && !hasRole("ADMIN") && !hasRole("CLINIC_ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            petFieldsDto.setUserId(userId);
            Pet created = petService.createPet(petFieldsDto);
            return new ResponseEntity<>(petMapper.toPetDto(created), HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage() == null ? "" : ex.getMessage();
            if (msg.contains("User not found")) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else if (msg.contains("Pet type not found")) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role));
    }
}
