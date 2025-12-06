package org.springframework.petmanagement.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ClinicTests {

    private Validator validator;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        validator = localValidatorFactoryBean;
    }

    @Test
    void shouldCreateValidClinic() {
        Clinic clinic = Clinic.builder()
            .id(UUID.randomUUID())
            .name("ペットクリニック東京")
            .telephone("03-1234-5678")
            .address("東京都渋谷区1-2-3")
            .websiteUrl("https://example.com")
            .openingHours("9:00-18:00")
            .note("予約制")
            .build();

        Set<ConstraintViolation<Clinic>> violations = validator.validate(clinic);
        assertThat(violations).isEmpty();
        assertThat(clinic.getName()).isEqualTo("ペットクリニック東京");
        assertThat(clinic.getTelephone()).isEqualTo("03-1234-5678");
    }

    @Test
    void shouldFailWhenNameIsNull() {
        Clinic clinic = Clinic.builder()
            .id(UUID.randomUUID())
            .name(null)
            .build();

        Set<ConstraintViolation<Clinic>> violations = validator.validate(clinic);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    @Test
    void shouldFailWhenNameIsBlank() {
        Clinic clinic = Clinic.builder()
            .id(UUID.randomUUID())
            .name("")
            .build();

        Set<ConstraintViolation<Clinic>> violations = validator.validate(clinic);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    @Test
    void shouldAllowNullOptionalFields() {
        Clinic clinic = Clinic.builder()
            .id(UUID.randomUUID())
            .name("シンプルクリニック")
            .build();

        Set<ConstraintViolation<Clinic>> violations = validator.validate(clinic);
        assertThat(violations).isEmpty();
        assertThat(clinic.getTelephone()).isNull();
        assertThat(clinic.getAddress()).isNull();
        assertThat(clinic.getWebsiteUrl()).isNull();
    }

    @Test
    void shouldSupportBuilderPattern() {
        Clinic clinic = Clinic.builder()
            .name("ビルダークリニック")
            .telephone("090-1111-2222")
            .build();

        assertThat(clinic).isNotNull();
        assertThat(clinic.getName()).isEqualTo("ビルダークリニック");
    }
}
