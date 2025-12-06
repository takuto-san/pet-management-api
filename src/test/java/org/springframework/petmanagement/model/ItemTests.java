package org.springframework.petmanagement.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ItemTests {

    private Validator validator;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        validator = localValidatorFactoryBean;
    }

    @Test
    void shouldCreateValidItem() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("manufacturer", "メーカーA");
        metadata.put("expiry", "2025-12-31");

        Item item = Item.builder()
            .id(UUID.randomUUID())
            .name("ドッグフード")
            .category(ItemCategory.FOOD)
            .note("高品質な栄養食")
            .metadata(metadata)
            .build();

        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        assertThat(violations).isEmpty();
        assertThat(item.getName()).isEqualTo("ドッグフード");
        assertThat(item.getCategory()).isEqualTo(ItemCategory.FOOD);
    }

    @Test
    void shouldFailWhenNameIsNull() {
        Item item = Item.builder()
            .id(UUID.randomUUID())
            .name(null)
            .category(ItemCategory.FOOD)
            .build();

        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    @Test
    void shouldFailWhenNameIsBlank() {
        Item item = Item.builder()
            .id(UUID.randomUUID())
            .name("")
            .category(ItemCategory.SUPPLEMENT)
            .build();

        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    @Test
    void shouldFailWhenCategoryIsNull() {
        Item item = Item.builder()
            .id(UUID.randomUUID())
            .name("テスト商品")
            .category(null)
            .build();

        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("category"));
    }

    @Test
    void shouldAllowNullOptionalFields() {
        Item item = Item.builder()
            .id(UUID.randomUUID())
            .name("シンプル商品")
            .category(ItemCategory.OTHER)
            .build();

        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        assertThat(violations).isEmpty();
        assertThat(item.getNote()).isNull();
        assertThat(item.getMetadata()).isNull();
    }

    @Test
    void shouldSupportAllItemCategories() {
        Item food = Item.builder()
            .name("フード")
            .category(ItemCategory.FOOD)
            .build();
        assertThat(food.getCategory()).isEqualTo(ItemCategory.FOOD);

        Item treat = Item.builder()
            .name("おやつ")
            .category(ItemCategory.TREAT)
            .build();
        assertThat(treat.getCategory()).isEqualTo(ItemCategory.TREAT);

        Item supplement = Item.builder()
            .name("サプリメント")
            .category(ItemCategory.SUPPLEMENT)
            .build();
        assertThat(supplement.getCategory()).isEqualTo(ItemCategory.SUPPLEMENT);

        Item other = Item.builder()
            .name("その他")
            .category(ItemCategory.OTHER)
            .build();
        assertThat(other.getCategory()).isEqualTo(ItemCategory.OTHER);
    }

    @Test
    void shouldSupportMetadataAsJsonb() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("color", "blue");
        metadata.put("size", "large");
        metadata.put("stock", 100);

        Item item = Item.builder()
            .name("カスタム商品")
            .category(ItemCategory.OTHER)
            .metadata(metadata)
            .build();

        assertThat(item.getMetadata()).isNotNull();
        assertThat(item.getMetadata().get("color")).isEqualTo("blue");
        assertThat(item.getMetadata().get("stock")).isEqualTo(100);
    }
}
