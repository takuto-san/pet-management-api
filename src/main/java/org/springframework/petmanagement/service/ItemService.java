package org.springframework.petmanagement.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.petmanagement.model.Item;
import org.springframework.petmanagement.rest.dto.ItemFieldsDto;

public interface ItemService {
    Page<Item> listItems(Pageable pageable);
    Optional<Item> getItem(UUID id);
    Item createItem(ItemFieldsDto fields);
    Item updateItem(UUID id, ItemFieldsDto fields);
    void deleteItem(UUID id);
}
