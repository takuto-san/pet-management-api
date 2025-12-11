package org.springframework.petmanagement.repository;

import java.util.Collection;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.petmanagement.model.Item;
import org.springframework.petmanagement.model.type.ItemType;

public interface ItemRepository {

    void save(Item item) throws DataAccessException;

    void delete(Item item) throws DataAccessException;

    Item findById(UUID id) throws DataAccessException;

    Collection<Item> findAll() throws DataAccessException;
    
    Collection<Item> findByCategory(ItemType category) throws DataAccessException;
}
