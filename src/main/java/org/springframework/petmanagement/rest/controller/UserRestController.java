package org.springframework.petmanagement.rest.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.petmanagement.mapper.UserMapper;
import org.springframework.petmanagement.model.Role;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.rest.api.UsersApi;
import org.springframework.petmanagement.rest.dto.UserDto;
import org.springframework.petmanagement.rest.dto.UserFieldsDto;
import org.springframework.petmanagement.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/api") 
public class UserRestController implements UsersApi {

    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder; 

    public UserRestController(UserService userService, 
                              UserMapper userMapper, 
                              PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @PreAuthorize("hasRole(@roles.ADMIN)")
    @Override
    public ResponseEntity<UserDto> addUser(UserFieldsDto userFieldsDto) {
        HttpHeaders headers = new HttpHeaders();
        
        User user = userMapper.toUser(userFieldsDto);
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (userFieldsDto.getRoles() != null && !userFieldsDto.getRoles().isEmpty()) {
            Set<Role> roles = userFieldsDto.getRoles().stream()
                .map(roleName -> {
                    Role role = new Role();
                    role.setRole(roleName);                     
                    role.setUser(user);     
                    return role;
                })
                .collect(Collectors.toSet());
            
            user.setRoles(roles);
        }
        
        this.userService.saveUser(user);
        
        UserDto userDto = userMapper.toUserDto(user);
        headers.setLocation(UriComponentsBuilder.newInstance()
            .path("/api/users/{id}").buildAndExpand(user.getId()).toUri());
            
        return new ResponseEntity<>(userDto, headers, HttpStatus.CREATED);
    }
}