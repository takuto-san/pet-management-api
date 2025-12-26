package org.springframework.petmanagement.model;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

class SpaceTests {

    private Validator validator;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        validator = localValidatorFactoryBean;
    }

    @Test
    void shouldCreateValidSpace() {
        UUID userId = UUID.randomUUID();
        Space space = Space.builder()
            .id(UUID.randomUUID())
            .userId(userId)
            .name("My Space")
            .build();

        Set<ConstraintViolation<Space>> violations = validator.validate(space);
        assertThat(violations).isEmpty();
        assertThat(space.getName()).isEqualTo("My Space");
        assertThat(space.getUserId()).isEqualTo(userId);
    }

    @Test
    void shouldFailWhenNameIsNull() {
        Space space = Space.builder()
            .id(UUID.randomUUID())
            .userId(UUID.randomUUID())
            .name(null)
            .build();

        Set<ConstraintViolation<Space>> violations = validator.validate(space);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    @Test
    void shouldFailWhenNameIsBlank() {
        Space space = Space.builder()
            .id(UUID.randomUUID())
            .userId(UUID.randomUUID())
            .name("")
            .build();

        Set<ConstraintViolation<Space>> violations = validator.validate(space);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    @Test
    void shouldFailWhenUserIdIsNull() {
        Space space = Space.builder()
            .id(UUID.randomUUID())
            .userId(null)
            .name("My Space")
            .build();

        Set<ConstraintViolation<Space>> violations = validator.validate(space);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("userId"));
    }

    @Test
    void shouldSupportBuilderPattern() {
        Space space = Space.builder()
            .name("Builder Space")
            .userId(UUID.randomUUID())
            .build();

        assertThat(space).isNotNull();
        assertThat(space.getName()).isEqualTo("Builder Space");
    }
}
