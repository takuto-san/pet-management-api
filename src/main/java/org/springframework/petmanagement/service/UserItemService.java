package org.springframework.petmanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.petmanagement.model.UserItem;
import org.springframework.petmanagement.rest.dto.UserItemFieldsDto;

public interface UserItemService {
    Page<UserItem> listUserItems(Pageable pageable);
    List<UserItem> getUserItemsByUserId(UUID userId);
    Optional<UserItem> getUserItem(UUID id);
    UserItem createUserItem(UserItemFieldsDto fields);
    UserItem updateUserItem(UUID id, UserItemFieldsDto fields);
    void deleteUserItem(UUID id);
}
