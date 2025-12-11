package org.springframework.petmanagement.service.itemService;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.petmanagement.model.Item;
import org.springframework.petmanagement.model.type.ItemType;
import org.springframework.petmanagement.service.ItemService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractItemServiceTests {

    @Autowired
    protected ItemService itemService;

    @Test
    void shouldListItemsEvenIfEmpty() {
        assertThat(itemService.findAll()).isNotNull();
    }

    @Test
    void shouldCreateItem() {
        Item item = new Item();
        item.setName("Test Item");
        item.setCategory(ItemType.MEDICAL);

        Item saved = itemService.save(item);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test Item");
    }

    @Test
    void shouldFindItemById() {
        Item item = new Item();
        item.setName("Test Item");
        item.setCategory(ItemType.MEDICAL);

        Item saved = itemService.save(item);
        assertThat(itemService.findById(saved.getId())).isPresent();
    }
}