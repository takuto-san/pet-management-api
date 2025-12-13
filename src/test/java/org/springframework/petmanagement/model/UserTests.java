package org.springframework.petmanagement.model;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

class UserTests {

    private Validator validator;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        validator = localValidatorFactoryBean;
    }

    @Test
    void shouldCreateValidUser() {
        User user = User.builder()
            .id(UUID.randomUUID())
            .username("johndoe")
            .password("securePassword123")
            .enabled(true)
            .firstName("太郎")
            .lastName("山田")
            .firstNameKana("タロウ")
            .lastNameKana("ヤマダ")
            .email("taro.yamada@example.com")
            .postalCode("1500001")
            .prefecture("東京都")
            .city("渋谷区")
            .address("神宮前1-2-3")
            .telephone("090-1234-5678")
            .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).isEmpty();
        assertThat(user.getUsername()).isEqualTo("johndoe");
        assertThat(user.getEmail()).isEqualTo("taro.yamada@example.com");
    }

    @Test
    void shouldFailWhenUsernameIsNull() {
        User user = User.builder()
            .username(null)
            .password("password")
            .firstName("太郎")
            .lastName("山田")
            .firstNameKana("タロウ")
            .lastNameKana("ヤマダ")
            .email("test@example.com")
            .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("username"));
    }

    @Test
    void shouldFailWhenUsernameIsBlank() {
        User user = User.builder()
            .username("")
            .password("password")
            .firstName("太郎")
            .lastName("山田")
            .firstNameKana("タロウ")
            .lastNameKana("ヤマダ")
            .email("test@example.com")
            .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("username"));
    }

    @Test
    void shouldFailWhenUsernameExceedsMaxLength() {
        User user = User.builder()
            .username("a".repeat(21))
            .password("password")
            .firstName("太郎")
            .lastName("山田")
            .firstNameKana("タロウ")
            .lastNameKana("ヤマダ")
            .email("test@example.com")
            .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("username"));
    }

    @Test
    void shouldFailWhenPasswordIsNull() {
        User user = User.builder()
            .username("johndoe")
            .password(null)
            .firstName("太郎")
            .lastName("山田")
            .firstNameKana("タロウ")
            .lastNameKana("ヤマダ")
            .email("test@example.com")
            .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    @Test
    void shouldFailWhenEmailIsInvalid() {
        User user = User.builder()
            .username("johndoe")
            .password("password")
            .firstName("太郎")
            .lastName("山田")
            .firstNameKana("タロウ")
            .lastNameKana("ヤマダ")
            .email("invalid-email")
            .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    void shouldFailWhenFirstNameIsNull() {
        User user = User.builder()
            .username("johndoe")
            .password("password")
            .firstName(null)
            .lastName("山田")
            .firstNameKana("タロウ")
            .lastNameKana("ヤマダ")
            .email("test@example.com")
            .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("firstName"));
    }

    @Test
    void shouldFailWhenTelephoneHasInvalidCharacters() {
        User user = User.builder()
            .username("johndoe")
            .password("password")
            .firstName("太郎")
            .lastName("山田")
            .firstNameKana("タロウ")
            .lastNameKana("ヤマダ")
            .email("test@example.com")
            .telephone("090-ABCD-5678")
            .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("telephone"));
    }

    @Test
    void shouldValidateTelephonePattern() {
        User validUser = User.builder()
            .username("johndoe")
            .password("password")
            .firstName("太郎")
            .lastName("山田")
            .firstNameKana("タロウ")
            .lastNameKana("ヤマダ")
            .email("test@example.com")
            .telephone("090-1234-5678")
            .build();

        Set<ConstraintViolation<User>> violations = validator.validate(validUser);
        assertThat(violations).isEmpty();

        User validUser2 = User.builder()
            .username("johndoe2")
            .password("password")
            .firstName("太郎")
            .lastName("山田")
            .firstNameKana("タロウ")
            .lastNameKana("ヤマダ")
            .email("test2@example.com")
            .telephone("09012345678")
            .build();

        violations = validator.validate(validUser2);
        assertThat(violations).isEmpty();
    }

    @Test
    void shouldAllowNullOptionalFields() {
        User user = User.builder()
            .username("simpleuser")
            .password("password")
            .firstName("太郎")
            .lastName("山田")
            .firstNameKana("タロウ")
            .lastNameKana("ヤマダ")
            .email("simple@example.com")
            .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).isEmpty();
        assertThat(user.getPostalCode()).isNull();
        assertThat(user.getPrefecture()).isNull();
        assertThat(user.getCity()).isNull();
        assertThat(user.getAddress()).isNull();
        assertThat(user.getTelephone()).isNull();
    }

    @Test
    void shouldSupportBuilderPattern() {
        User user = User.builder()
            .username("builder")
            .password("password")
            .firstName("太郎")
            .lastName("山田")
            .firstNameKana("タロウ")
            .lastNameKana("ヤマダ")
            .email("builder@example.com")
            .build();

        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("builder");
    }

    @Test
    void shouldHaveDefaultEnabledValue() {
        User user = User.builder()
            .username("testuser")
            .password("password")
            .firstName("太郎")
            .lastName("山田")
            .firstNameKana("タロウ")
            .lastNameKana("ヤマダ")
            .email("test@example.com")
            .build();

        assertThat(user.getEnabled()).isTrue();
    }

    @Test
    void shouldInitializeRolesEmpty() {
        User user = User.builder()
            .username("testuser")
            .password("password")
            .firstName("太郎")
            .lastName("山田")
            .firstNameKana("タロウ")
            .lastNameKana("ヤマダ")
            .email("test@example.com")
            .build();

        assertThat(user.getRoles()).isNotNull();
        assertThat(user.getRoles()).isEmpty();
    }
}