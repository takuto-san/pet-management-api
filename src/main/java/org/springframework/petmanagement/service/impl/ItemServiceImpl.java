package org.springframework.petmanagement.service.impl;

import org.springframework.petmanagement.model.Item;
import org.springframework.petmanagement.repository.ItemRepository;
import org.springframework.petmanagement.service.ItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> findAll() {
        return new ArrayList<>(itemRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Item> findById(UUID id) {
        return Optional.ofNullable(itemRepository.findById(id));
    }

    @Override
    public Item save(Item item) {
        itemRepository.save(item);
        return item;
    }

    @Override
    public void deleteItem(UUID id) {
        Item item = itemRepository.findById(id);
        if (item == null) throw new IllegalArgumentException("item not found");
        itemRepository.delete(item);
    }
}
