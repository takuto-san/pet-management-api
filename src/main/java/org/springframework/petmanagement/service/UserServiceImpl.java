package org.springframework.petmanagement.service;

import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.model.Role;
import org.springframework.petmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void saveUser(User user) throws DataAccessException {
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            throw new IllegalArgumentException("User must have at least a role set!");
        }

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        for (Role role : user.getRoles()) {
            if (!role.getRole().startsWith("ROLE_")) {
                role.setRole("ROLE_" + role.getRole());
            }

            if (role.getUser() == null) {
                role.setUser(user);
            }
        }

        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUserById(UUID id) throws DataAccessException {
        return Optional.ofNullable(userRepository.findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUserByUsername(String username) throws DataAccessException {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<User> findAllUsers() throws DataAccessException {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteUser(User user) throws DataAccessException {
        userRepository.delete(user);
    }
}