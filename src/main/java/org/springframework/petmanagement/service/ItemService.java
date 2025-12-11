package org.springframework.petmanagement.service;

import org.springframework.petmanagement.model.Item;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ItemService {
    List<Item> findAll();
    Optional<Item> findById(UUID id);
    Item save(Item item);
    void deleteItem(UUID id);
}
