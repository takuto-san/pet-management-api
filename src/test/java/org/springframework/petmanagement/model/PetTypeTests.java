package org.springframework.petmanagement.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PetTypeTests {

    private Validator validator;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        validator = localValidatorFactoryBean;
    }

    @Test
    void shouldCreateValidPetType() {
        PetType petType = PetType.builder()
            .id(UUID.randomUUID())
            .name("dog")
            .build();

        Set<ConstraintViolation<PetType>> violations = validator.validate(petType);
        assertThat(violations).isEmpty();
        assertThat(petType.getName()).isEqualTo("dog");
    }

    @Test
    void shouldFailWhenNameIsNull() {
        PetType petType = PetType.builder()
            .id(UUID.randomUUID())
            .name(null)
            .build();

        Set<ConstraintViolation<PetType>> violations = validator.validate(petType);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    @Test
    void shouldFailWhenNameIsBlank() {
        PetType petType = PetType.builder()
            .id(UUID.randomUUID())
            .name("")
            .build();

        Set<ConstraintViolation<PetType>> violations = validator.validate(petType);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    @Test
    void shouldSupportBuilderPattern() {
        PetType petType = PetType.builder()
            .name("cat")
            .build();

        assertThat(petType).isNotNull();
        assertThat(petType.getName()).isEqualTo("cat");
    }

    @Test
    void shouldSupportCommonPetTypes() {
        PetType dog = PetType.builder().name("dog").build();
        assertThat(dog.getName()).isEqualTo("dog");

        PetType cat = PetType.builder().name("cat").build();
        assertThat(cat.getName()).isEqualTo("cat");

        PetType bird = PetType.builder().name("bird").build();
        assertThat(bird.getName()).isEqualTo("bird");

        PetType reptile = PetType.builder().name("reptile").build();
        assertThat(reptile.getName()).isEqualTo("reptile");
    }
}
