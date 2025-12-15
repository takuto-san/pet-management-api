package org.springframework.petmanagement.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.lang.Nullable;

import org.springframework.lang.Nullable;
import org.springframework.petmanagement.mapper.UserMapper;
import org.springframework.petmanagement.model.Role;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.model.type.RoleType;
import org.springframework.petmanagement.repository.RoleRepository;
import org.springframework.petmanagement.repository.UserRepository;
import org.springframework.petmanagement.rest.dto.AdminUserUpdateDto;
import org.springframework.petmanagement.rest.dto.RoleNameDto;
import org.springframework.petmanagement.rest.dto.UserBaseDto;
import org.springframework.petmanagement.rest.dto.UserRegistrationDto;
import org.springframework.petmanagement.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           UserMapper userMapper,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(UserRegistrationDto fields) {
        if (fields.getUsername() == null || fields.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("username cannot be blank");
        }
        if (userRepository.findByUsername(fields.getUsername()).isPresent()) {
            throw new IllegalArgumentException("username already exists");
        }

        User user = userMapper.toUser(fields);

        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(fields.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role defaultRole = roleRepository.findByName(RoleType.OWNER)
                .orElseThrow(() -> new IllegalStateException("Default role OWNER not found in DB"));
        roles.add(defaultRole);
        user.setRoles(roles);

        userRepository.save(user);
        return user;
    }

    @Override
    public User updateUser(UUID userId, AdminUserUpdateDto fields) {
        User current = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user not found"));

        userMapper.updateUserFromFields(fields, current);

        if (fields.getEnabled() != null) {
            current.setEnabled(fields.getEnabled());
        }

        if (fields.getRoles() != null) {
            Set<Role> newRoles = new HashSet<>();
            for (RoleNameDto roleNameDto : fields.getRoles()) {
                Role role = roleRepository.findByName(RoleType.valueOf(roleNameDto.name()))
                        .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleNameDto));
                newRoles.add(role);
            }
            current.setRoles(newRoles);
        }

        userRepository.save(current);
        return current;
    }

    @Override
    public User updateUserBase(UUID userId, UserBaseDto fields) {
        User current = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user not found"));

        userMapper.updateUserFromBase(fields, current);

        userRepository.save(current);
        return current;
    }

    @Override
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user not found"));
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> listUsersByName(@Nullable String lastNameKana, @Nullable String firstNameKana) {
        List<User> all = (List<User>) userRepository.findAll();

        if (lastNameKana == null && firstNameKana == null) return all;

        return all.stream()
            .filter(u -> lastNameKana == null || (u.getLastNameKana() != null && u.getLastNameKana().contains(lastNameKana)))
            .filter(u -> firstNameKana == null || (u.getFirstNameKana() != null && u.getFirstNameKana().contains(firstNameKana)))
            .collect(Collectors.toList());
    }
}
