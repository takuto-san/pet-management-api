package org.springframework.petmanagement.rest.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.petmanagement.mapper.ItemMapper;
import org.springframework.petmanagement.model.Item;
import org.springframework.petmanagement.rest.api.ItemsApi;
import org.springframework.petmanagement.rest.dto.ItemDto;
import org.springframework.petmanagement.rest.dto.ItemFieldsDto;
import org.springframework.petmanagement.rest.dto.ItemPageDto;
import org.springframework.petmanagement.service.ItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/api")
public class ItemController implements ItemsApi {

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    public ItemController(ItemService itemService, ItemMapper itemMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }

    @PreAuthorize("hasAnyRole(@roles.ADMIN, @roles.CLINIC_ADMIN)")
    @Override
    public ResponseEntity<ItemPageDto> listItems(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> items = itemService.listItems(pageable);
        return ResponseEntity.ok(itemMapper.toItemPageDto(items));
    }

    @PreAuthorize("hasRole(@roles.ADMIN)")
    @Override
    public ResponseEntity<ItemDto> addItem(ItemFieldsDto itemFieldsDto) {
        Item saved = itemService.createItem(itemFieldsDto);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(
            UriComponentsBuilder.newInstance()
                .path("/api/items/{id}")
                .buildAndExpand(saved.getId())
                .toUri()
        );
        return new ResponseEntity<>(itemMapper.toItemDto(saved), headers, HttpStatus.CREATED);
    }
}
