package org.springframework.petmanagement.model;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

class UserItemTests {

    private Validator validator;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        validator = localValidatorFactoryBean;
    }

    @Test
    void shouldCreateValidUserItem() {
        User user = User.builder()
            .id(UUID.randomUUID())
            .username("testuser")
            .email("test@example.com")
            .build();

        Item item = Item.builder()
            .id(UUID.randomUUID())
            .name("Test Item")
            .category(org.springframework.petmanagement.model.type.ItemType.FOOD)
            .build();

        UserItem userItem = UserItem.builder()
            .id(UUID.randomUUID())
            .user(user)
            .item(item)
            .note("My favorite item")
            .recordedAt(LocalDateTime.now())
            .build();

        Set<ConstraintViolation<UserItem>> violations = validator.validate(userItem);
        assertThat(violations).isEmpty();
        assertThat(userItem.getUser()).isNotNull();
        assertThat(userItem.getItem()).isNotNull();
        assertThat(userItem.getNote()).isEqualTo("My favorite item");
        assertThat(userItem.getRecordedAt()).isNotNull();
    }

    @Test
    void shouldFailWhenUserIsNull() {
        Item item = Item.builder()
            .id(UUID.randomUUID())
            .name("Test Item")
            .category(org.springframework.petmanagement.model.type.ItemType.FOOD)
            .build();

        UserItem userItem = UserItem.builder()
            .id(UUID.randomUUID())
            .user(null)
            .item(item)
            .build();

        Set<ConstraintViolation<UserItem>> violations = validator.validate(userItem);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("user"));
    }

    @Test
    void shouldFailWhenItemIsNull() {
        User user = User.builder()
            .id(UUID.randomUUID())
            .username("testuser")
            .email("test@example.com")
            .build();

        UserItem userItem = UserItem.builder()
            .id(UUID.randomUUID())
            .user(user)
            .item(null)
            .build();

        Set<ConstraintViolation<UserItem>> violations = validator.validate(userItem);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("item"));
    }

    @Test
    void shouldAllowNullNote() {
        User user = User.builder()
            .id(UUID.randomUUID())
            .username("testuser")
            .email("test@example.com")
            .build();

        Item item = Item.builder()
            .id(UUID.randomUUID())
            .name("Test Item")
            .category(org.springframework.petmanagement.model.type.ItemType.FOOD)
            .build();

        UserItem userItem = UserItem.builder()
            .id(UUID.randomUUID())
            .user(user)
            .item(item)
            .note(null)
            .build();

        Set<ConstraintViolation<UserItem>> violations = validator.validate(userItem);
        assertThat(violations).isEmpty();
        assertThat(userItem.getNote()).isNull();
    }

    @Test
    void shouldAllowNullRecordedAt() {
        User user = User.builder()
            .id(UUID.randomUUID())
            .username("testuser")
            .email("test@example.com")
            .build();

        Item item = Item.builder()
            .id(UUID.randomUUID())
            .name("Test Item")
            .category(org.springframework.petmanagement.model.type.ItemType.FOOD)
            .build();

        UserItem userItem = UserItem.builder()
            .id(UUID.randomUUID())
            .user(user)
            .item(item)
            .recordedAt(null)
            .build();

        Set<ConstraintViolation<UserItem>> violations = validator.validate(userItem);
        assertThat(violations).isEmpty();
        assertThat(userItem.getRecordedAt()).isNull();
    }
}
