package org.springframework.petmanagement.service.userItemService;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.petmanagement.model.Item;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.model.UserItem;
import org.springframework.petmanagement.rest.dto.ItemCategoryDto;
import org.springframework.petmanagement.rest.dto.ItemFieldsDto;
import org.springframework.petmanagement.rest.dto.UserItemFieldsDto;
import org.springframework.petmanagement.rest.dto.UserRegistrationDto;
import org.springframework.petmanagement.service.ItemService;
import org.springframework.petmanagement.service.UserItemService;
import org.springframework.petmanagement.service.UserService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractUserItemServiceTests {

    @Autowired
    protected UserItemService userItemService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected ItemService itemService;

    @Test
    void shouldFindAll() {
        assertThat(userItemService.listUserItems(PageRequest.of(0, 10))).isNotNull();
    }

    @Test
    void shouldCreateUserItem() {
        // Create test user and item first
        User user = createTestUser();
        Item item = createTestItem();

        UserItemFieldsDto fields = new UserItemFieldsDto()
            .userId(user.getId())
            .itemId(item.getId())
            .note("Test user item");

        UserItem saved = userItemService.createUserItem(fields);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUser().getId()).isEqualTo(user.getId());
        assertThat(saved.getItem().getId()).isEqualTo(item.getId());
        assertThat(saved.getNote()).isEqualTo("Test user item");
        assertThat(saved.getRecordedAt()).isNotNull();
    }

    @Test
    void shouldFindUserItemById() {
        // Create test user and item first
        User user = createTestUser();
        Item item = createTestItem();

        UserItemFieldsDto fields = new UserItemFieldsDto()
            .userId(user.getId())
            .itemId(item.getId())
            .note("Test user item");

        UserItem saved = userItemService.createUserItem(fields);
        assertThat(userItemService.getUserItem(saved.getId())).isPresent();
    }

    @Test
    void shouldFindUserItemsByUserId() {
        // Create test user and items first
        User user = createTestUser();
        Item item1 = createTestItem();
        Item item2 = createTestItem();

        // Create user items
        UserItemFieldsDto fields1 = new UserItemFieldsDto()
            .userId(user.getId())
            .itemId(item1.getId())
            .note("Item 1");

        UserItemFieldsDto fields2 = new UserItemFieldsDto()
            .userId(user.getId())
            .itemId(item2.getId())
            .note("Item 2");

        userItemService.createUserItem(fields1);
        userItemService.createUserItem(fields2);

        List<UserItem> userItems = userItemService.getUserItemsByUserId(user.getId());
        assertThat(userItems).hasSize(2);
        assertThat(userItems).allMatch(ui -> ui.getUser().getId().equals(user.getId()));
    }

    @Test
    void shouldUpdateUserItem() {
        // Create test user and item first
        User user = createTestUser();
        Item item = createTestItem();

        UserItemFieldsDto createFields = new UserItemFieldsDto()
            .userId(user.getId())
            .itemId(item.getId())
            .note("Original note");

        UserItem saved = userItemService.createUserItem(createFields);

        UserItemFieldsDto updateFields = new UserItemFieldsDto()
            .userId(user.getId())
            .itemId(item.getId())
            .note("Updated note");

        UserItem updated = userItemService.updateUserItem(saved.getId(), updateFields);
        assertThat(updated.getNote()).isEqualTo("Updated note");
    }

    @Test
    void shouldDeleteUserItem() {
        // Create test user and item first
        User user = createTestUser();
        Item item = createTestItem();

        UserItemFieldsDto fields = new UserItemFieldsDto()
            .userId(user.getId())
            .itemId(item.getId())
            .note("Test user item");

        UserItem saved = userItemService.createUserItem(fields);
        UUID userItemId = saved.getId();

        userItemService.deleteUserItem(userItemId);
        assertThat(userItemService.getUserItem(userItemId)).isEmpty();
    }

    private User createTestUser() {
        // Create a test user with required fields
        long time = System.currentTimeMillis();
        var fields = new UserRegistrationDto()
            .username("test" + (time % 10000))
            .email("test" + time + "@example.com")
            .password("TestPass123");
        return userService.createUser(fields);
    }

    private Item createTestItem() {
        // Create a test item using ItemService
        var fields = new ItemFieldsDto()
            .name("Test Item")
            .category(ItemCategoryDto.FOOD);
        return itemService.createItem(fields);
    }
}
