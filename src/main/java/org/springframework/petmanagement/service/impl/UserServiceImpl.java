package org.springframework.petmanagement.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.lang.Nullable;
import org.springframework.petmanagement.mapper.UserMapper;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.repository.UserRepository;
import org.springframework.petmanagement.rest.dto.UserFieldsDto;
import org.springframework.petmanagement.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(UserFieldsDto fields) {
        if (fields.getUsername() == null || fields.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("username cannot be blank");
        }
        if (userRepository.findByUsername(fields.getUsername()) != null) {
            throw new IllegalArgumentException("username already exists");
        }
        User user = userMapper.toUser(fields);
        user.setEnabled(fields.getEnabled() == null || fields.getEnabled());
        user.setRole(fields.getRole() != null ? fields.getRole() : "user");
        user.setPassword(passwordEncoder.encode(fields.getPassword()));
        userRepository.save(user);
        return user;
    }

    @Override
    public User updateUser(UUID userId, UserFieldsDto fields) {
        User current = userRepository.findById(userId);
        if (current == null) throw new IllegalArgumentException("user not found");

        userMapper.updateUserFromFields(fields, current);
        if (fields.getEnabled() != null) current.setEnabled(fields.getEnabled());
        if (fields.getRole() != null) current.setRole(fields.getRole());
        if (fields.getPassword() != null) {
            current.setPassword(passwordEncoder.encode(fields.getPassword()));
        }
        userRepository.save(current);
        return current;
    }

    @Override
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId);
        if (user == null) throw new IllegalArgumentException("user not found");
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(UUID userId) {
        return Optional.ofNullable(userRepository.findById(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> search(@Nullable String lastNameKana, @Nullable String firstNameKana) {
        List<User> all = new ArrayList<>(userRepository.findAll());
        if (lastNameKana == null && firstNameKana == null) return all;

        return all.stream()
            .filter(u -> lastNameKana == null || (u.getLastNameKana() != null && u.getLastNameKana().contains(lastNameKana)))
            .filter(u -> firstNameKana == null || (u.getFirstNameKana() != null && u.getFirstNameKana().contains(firstNameKana)))
            .toList();
    }
}
