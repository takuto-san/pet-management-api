package org.springframework.petmanagement.service.impl;

import org.springframework.lang.Nullable;
import org.springframework.petmanagement.mapper.PetMapper;
import org.springframework.petmanagement.mapper.UserMapper;
import org.springframework.petmanagement.model.Pet;
import org.springframework.petmanagement.model.PetType;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.repository.PetRepository;
import org.springframework.petmanagement.repository.PetTypeRepository;
import org.springframework.petmanagement.repository.UserRepository;
import org.springframework.petmanagement.rest.dto.PetFieldsDto;
import org.springframework.petmanagement.rest.dto.UserFieldsDto;
import org.springframework.petmanagement.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final PetTypeRepository petTypeRepository;
    private final UserMapper userMapper;
    private final PetMapper petMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PetRepository petRepository,
                           PetTypeRepository petTypeRepository,
                           UserMapper userMapper,
                           PetMapper petMapper,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.petRepository = petRepository;
        this.petTypeRepository = petTypeRepository;
        this.userMapper = userMapper;
        this.petMapper = petMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(UserFieldsDto fields) {
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

    @Override
    public Pet addPetToUser(UUID userId, PetFieldsDto fields) {
        User user = userRepository.findById(userId);
        if (user == null) throw new IllegalArgumentException("user not found");

        PetType type = petTypeRepository.findById(fields.getTypeId());
        if (type == null) throw new IllegalArgumentException("pet type not found");

        Pet pet = petMapper.toPet(fields);
        pet.setUser(user);
        pet.setType(type);
        petRepository.save(pet);
        return pet;
    }

    @Override
    public Pet updateUsersPet(UUID userId, UUID petId, PetFieldsDto fields) {
        User user = userRepository.findById(userId);
        if (user == null) throw new IllegalArgumentException("user not found");

        Pet pet = petRepository.findById(petId);
        if (pet == null || !pet.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("pet not found for this user");
        }

        PetType type = petTypeRepository.findById(fields.getTypeId());
        if (type == null) throw new IllegalArgumentException("pet type not found");

        petMapper.updatePetFromFields(fields, pet);
        pet.setType(type);
        pet.setUser(user);
        petRepository.save(pet);
        return pet;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pet> findUsersPet(UUID userId, UUID petId) {
        User user = userRepository.findById(userId);
        if (user == null) return Optional.empty();
        
        Pet pet = petRepository.findById(petId);
        if (pet == null || !pet.getUser().getId().equals(userId)) {
            return Optional.empty();
        }
        return Optional.of(pet);
    }
}