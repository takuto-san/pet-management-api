package org.springframework.petmanagement.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.petmanagement.model.type.ItemType;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

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
            .category(ItemType.FOOD) 
            .note("高品質な栄養食")
            .metadata(metadata)
            .build();

        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        assertThat(violations).isEmpty();
        assertThat(item.getName()).isEqualTo("ドッグフード");
        assertThat(item.getCategory()).isEqualTo(ItemType.FOOD); 
    }

    @Test
    void shouldFailWhenNameIsNull() {
        Item item = Item.builder()
            .id(UUID.randomUUID())
            .name(null)
            .category(ItemType.FOOD) 
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
            .category(ItemType.SUPPLEMENT) 
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
            .category(ItemType.OTHER) 
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
            .category(ItemType.FOOD) 
            .build();
        assertThat(food.getCategory()).isEqualTo(ItemType.FOOD); 

        Item treat = Item.builder()
            .name("おやつ")
            .category(ItemType.TREAT) 
            .build();
        assertThat(treat.getCategory()).isEqualTo(ItemType.TREAT); 

        Item supplement = Item.builder()
            .name("サプリメント")
            .category(ItemType.SUPPLEMENT) 
            .build();
        assertThat(supplement.getCategory()).isEqualTo(ItemType.SUPPLEMENT); 

        Item other = Item.builder()
            .name("その他")
            .category(ItemType.OTHER) 
            .build();
        assertThat(other.getCategory()).isEqualTo(ItemType.OTHER); 
    }

    @Test
    void shouldSupportMetadataAsJsonb() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("color", "blue");
        metadata.put("size", "large");
        metadata.put("stock", 100);

        Item item = Item.builder()
            .name("カスタム商品")
            .category(ItemType.OTHER) 
            .metadata(metadata)
            .build();

        assertThat(item.getMetadata()).isNotNull();
        assertThat(item.getMetadata().get("color")).isEqualTo("blue");
        assertThat(item.getMetadata().get("stock")).isEqualTo(100);
    }
}