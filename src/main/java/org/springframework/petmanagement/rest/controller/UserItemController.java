package org.springframework.petmanagement.rest.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.petmanagement.mapper.UserItemMapper;
import org.springframework.petmanagement.model.UserItem;
import org.springframework.petmanagement.rest.api.UserItemsApi;
import org.springframework.petmanagement.rest.dto.UserItemDto;
import org.springframework.petmanagement.rest.dto.UserItemFieldsDto;
import org.springframework.petmanagement.rest.dto.UserItemPageDto;
import org.springframework.petmanagement.service.UserItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
public class UserItemController implements UserItemsApi {

    private final UserItemService userItemService;
    private final UserItemMapper userItemMapper;

    public UserItemController(UserItemService userItemService, UserItemMapper userItemMapper) {
        this.userItemService = userItemService;
        this.userItemMapper = userItemMapper;
    }

    @PreAuthorize("hasAnyRole(@roles.OWNER, @roles.ADMIN)")
    @Override
    public ResponseEntity<UserItemPageDto> listUserItems(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserItem> userItems = userItemService.listUserItems(pageable);
        return ResponseEntity.ok(userItemMapper.toUserItemPageDto(userItems));
    }

    @PreAuthorize("hasAnyRole(@roles.OWNER, @roles.ADMIN)")
    @Override
    public ResponseEntity<UserItemDto> addUserItem(UserItemFieldsDto userItemFieldsDto) {
        UserItem saved = userItemService.createUserItem(userItemFieldsDto);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(
            UriComponentsBuilder.newInstance()
                .path("/user-items/{id}")
                .buildAndExpand(saved.getId())
                .toUri()
        );
        return new ResponseEntity<>(userItemMapper.toUserItemDto(saved), headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole(@roles.OWNER, @roles.ADMIN)")
    @Override
    public ResponseEntity<UserItemDto> getUserItem(java.util.UUID userItemId) {
        var userItem = userItemService.getUserItem(userItemId);
        if (userItem.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userItemMapper.toUserItemDto(userItem.get()));
    }

    @PreAuthorize("hasAnyRole(@roles.OWNER, @roles.ADMIN)")
    @Override
    public ResponseEntity<UserItemDto> updateUserItem(java.util.UUID userItemId, UserItemFieldsDto userItemFieldsDto) {
        UserItem updated = userItemService.updateUserItem(userItemId, userItemFieldsDto);
        return ResponseEntity.ok(userItemMapper.toUserItemDto(updated));
    }

    @PreAuthorize("hasAnyRole(@roles.OWNER, @roles.ADMIN)")
    @Override
    public ResponseEntity<Void> deleteUserItem(java.util.UUID userItemId) {
        userItemService.deleteUserItem(userItemId);
        return ResponseEntity.noContent().build();
    }
}
