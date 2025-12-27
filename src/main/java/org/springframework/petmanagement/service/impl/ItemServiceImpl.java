package org.springframework.petmanagement.service.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.petmanagement.mapper.ItemMapper;
import org.springframework.petmanagement.model.Item;
import org.springframework.petmanagement.repository.ItemRepository;
import org.springframework.petmanagement.rest.dto.ItemFieldsDto;
import org.springframework.petmanagement.service.ItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public ItemServiceImpl(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Item> listItems(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Item> getItem(UUID id) {
        return Optional.ofNullable(itemRepository.findById(id));
    }

    @Override
    public Item createItem(ItemFieldsDto fields) {
        Item item = itemMapper.toItem(fields);
        itemRepository.save(item);
        return item;
    }

    @Override
    public Item updateItem(UUID id, ItemFieldsDto fields) {
        Item item = itemRepository.findById(id);
        if (item == null) {
            throw new IllegalArgumentException("Item not found: " + id);
        }
        itemMapper.updateItemFromFields(fields, item);
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
