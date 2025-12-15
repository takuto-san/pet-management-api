package org.springframework.petmanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.petmanagement.model.Item;
import org.springframework.petmanagement.rest.dto.ItemFieldsDto;

public interface ItemService {
    List<Item> listItems();
    Optional<Item> getItem(UUID id);
    Item createItem(ItemFieldsDto fields);
    Item updateItem(UUID id, ItemFieldsDto fields);
    void deleteItem(UUID id);
}
