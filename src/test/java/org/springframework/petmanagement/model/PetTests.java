package org.springframework.petmanagement.model;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.petmanagement.model.type.PetSex;
import org.springframework.petmanagement.model.type.PetType;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

class PetTests {

    private Validator validator;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        validator = localValidatorFactoryBean;
    }

    @Test
    void shouldCreateValidPet() {
        PetType petType = PetType.DOG;
        User user = User.builder().id(UUID.randomUUID()).username("test").email("test@example.com").firstName("Test").lastName("User").build();

        Pet pet = Pet.builder()
            .id(UUID.randomUUID())
            .name("ポチ")
            .birthDate(LocalDate.of(2020, 1, 1))
            .sex(PetSex.MALE)
            .type(petType)
            .user(user)
            .build();

        Set<ConstraintViolation<Pet>> violations = validator.validate(pet);
        assertThat(violations).isEmpty();
        assertThat(pet.getName()).isEqualTo("ポチ");
        assertThat(pet.getBirthDate()).isEqualTo(LocalDate.of(2020, 1, 1));
    }

    @Test
    void shouldFailWhenNameIsNull() {
        PetType petType = PetType.DOG;
        User user = User.builder().id(UUID.randomUUID()).username("test").email("test@example.com").firstName("Test").lastName("User").build();

        Pet pet = Pet.builder()
            .id(UUID.randomUUID())
            .name(null)
            .type(petType)
            .user(user)
            .build();

        Set<ConstraintViolation<Pet>> violations = validator.validate(pet);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    @Test
    void shouldFailWhenNameIsBlank() {
        PetType petType = PetType.DOG;
        User user = User.builder().id(UUID.randomUUID()).username("test").email("test@example.com").firstName("Test").lastName("User").build();

        Pet pet = Pet.builder()
            .id(UUID.randomUUID())
            .name("")
            .type(petType)
            .user(user)
            .build();

        Set<ConstraintViolation<Pet>> violations = validator.validate(pet);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    @Test
    void shouldFailWhenNameExceedsMaxLength() {
        PetType petType = PetType.DOG;
        User user = User.builder().id(UUID.randomUUID()).username("test").email("test@example.com").firstName("Test").lastName("User").build();

        Pet pet = Pet.builder()
            .id(UUID.randomUUID())
            .name("a".repeat(31))
            .type(petType)
            .user(user)
            .build();

        Set<ConstraintViolation<Pet>> violations = validator.validate(pet);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    @Test
    void shouldFailWhenTypeIsNull() {
        User user = User.builder().id(UUID.randomUUID()).username("test").email("test@example.com").firstName("Test").lastName("User").build();

        Pet pet = Pet.builder()
            .id(UUID.randomUUID())
            .name("ポチ")
            .type(null)
            .user(user)
            .build();

        Set<ConstraintViolation<Pet>> violations = validator.validate(pet);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("type"));
    }

    @Test
    void shouldFailWhenUserIsNull() {
        PetType petType = PetType.DOG;

        Pet pet = Pet.builder()
            .id(UUID.randomUUID())
            .name("ポチ")
            .type(petType)
            .user(null)
            .build();

        Set<ConstraintViolation<Pet>> violations = validator.validate(pet);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("user"));
    }

    @Test
    void shouldAllowNullOptionalFields() {
        PetType petType = PetType.DOG;
        User user = User.builder().id(UUID.randomUUID()).username("test").email("test@example.com").firstName("Test").lastName("User").build();

        Pet pet = Pet.builder()
            .id(UUID.randomUUID())
            .name("タマ")
            .type(petType)
            .user(user)
            .build();

        Set<ConstraintViolation<Pet>> violations = validator.validate(pet);
        assertThat(violations).isEmpty();
        assertThat(pet.getBirthDate()).isNull();
        assertThat(pet.getSex()).isNull();
    }

    @Test
    void shouldSupportBuilderPattern() {
        PetType petType = PetType.CAT;
        User user = User.builder().id(UUID.randomUUID()).username("test").email("test@example.com").firstName("Test").lastName("User").build();

        Pet pet = Pet.builder()
            .name("ミケ")
            .type(petType)
            .user(user)
            .build();

        assertThat(pet).isNotNull();
        assertThat(pet.getName()).isEqualTo("ミケ");
    }
}
