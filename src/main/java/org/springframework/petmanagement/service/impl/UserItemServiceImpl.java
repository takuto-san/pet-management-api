package org.springframework.petmanagement.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.petmanagement.mapper.UserItemMapper;
import org.springframework.petmanagement.model.Item;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.model.UserItem;
import org.springframework.petmanagement.repository.UserItemRepository;
import org.springframework.petmanagement.rest.dto.UserItemFieldsDto;
import org.springframework.petmanagement.service.ItemService;
import org.springframework.petmanagement.service.UserItemService;
import org.springframework.petmanagement.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserItemServiceImpl implements UserItemService {

    private final UserItemRepository userItemRepository;
    private final UserItemMapper userItemMapper;
    private final UserService userService;
    private final ItemService itemService;

    public UserItemServiceImpl(UserItemRepository userItemRepository, UserItemMapper userItemMapper,
                               UserService userService, ItemService itemService) {
        this.userItemRepository = userItemRepository;
        this.userItemMapper = userItemMapper;
        this.userService = userService;
        this.itemService = itemService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserItem> listUserItems(Pageable pageable) {
        return userItemRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserItem> getUserItemsByUserId(UUID userId) {
        return (List<UserItem>) userItemRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserItem> getUserItem(UUID id) {
        return Optional.ofNullable(userItemRepository.findById(id));
    }

    @Override
    public UserItem createUserItem(UserItemFieldsDto fields) {
        UserItem userItem = userItemMapper.toUserItem(fields);

        // Set user
        User user = userService.getUser(fields.getUserId());
        userItem.setUser(user);

        // Set item
        Optional<Item> itemOpt = itemService.getItem(fields.getItemId());
        if (itemOpt.isEmpty()) {
            throw new IllegalArgumentException("Item not found: " + fields.getItemId());
        }
        userItem.setItem(itemOpt.get());

        // Set recordedAt if not provided
        if (userItem.getRecordedAt() == null) {
            userItem.setRecordedAt(LocalDateTime.now());
        }

        userItemRepository.save(userItem);
        return userItem;
    }

    @Override
    public UserItem updateUserItem(UUID id, UserItemFieldsDto fields) {
        UserItem userItem = userItemRepository.findById(id);
        if (userItem == null) {
            throw new IllegalArgumentException("UserItem not found: " + id);
        }
        userItemMapper.updateUserItemFromFields(fields, userItem);

        // Update user if provided
        if (fields.getUserId() != null) {
            User user = userService.getUser(fields.getUserId());
            userItem.setUser(user);
        }

        // Update item if provided
        if (fields.getItemId() != null) {
            Optional<Item> itemOpt = itemService.getItem(fields.getItemId());
            if (itemOpt.isEmpty()) {
                throw new IllegalArgumentException("Item not found: " + fields.getItemId());
            }
            userItem.setItem(itemOpt.get());
        }

        userItemRepository.save(userItem);
        return userItem;
    }

    @Override
    public void deleteUserItem(UUID id) {
        UserItem userItem = userItemRepository.findById(id);
        if (userItem == null) throw new IllegalArgumentException("UserItem not found");
        userItemRepository.delete(userItem);
    }
}
