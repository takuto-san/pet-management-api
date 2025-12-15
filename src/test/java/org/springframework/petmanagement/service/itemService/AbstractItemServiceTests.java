package org.springframework.petmanagement.service.itemService;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.petmanagement.model.Item;
import org.springframework.petmanagement.rest.dto.ItemCategoryDto;
import org.springframework.petmanagement.rest.dto.ItemFieldsDto;
import org.springframework.petmanagement.service.ItemService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractItemServiceTests {

    @Autowired
    protected ItemService itemService;

    @Test
    void shouldFindAll() {
        assertThat(itemService.listItems(PageRequest.of(0, 10))).isNotNull();
    }

    @Test
    void shouldCreateItem() {
        ItemFieldsDto fields = new ItemFieldsDto()
            .name("Test Item")
            .category(ItemCategoryDto.FOOD);

        Item saved = itemService.createItem(fields);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test Item");
    }

    @Test
    void shouldFindItemById() {
        ItemFieldsDto fields = new ItemFieldsDto()
            .name("Test Item")
            .category(ItemCategoryDto.FOOD);

        Item saved = itemService.createItem(fields);
        assertThat(itemService.getItem(saved.getId())).isPresent();
    }
}
