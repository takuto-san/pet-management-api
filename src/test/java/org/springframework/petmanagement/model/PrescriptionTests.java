package org.springframework.petmanagement.model;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.petmanagement.model.type.MedicationType;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

class PrescriptionTests {

    private Validator validator;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        validator = localValidatorFactoryBean;
    }

    @Test
    void shouldCreateValidPrescription() {
        Prescription prescription = Prescription.builder()
            .id(UUID.randomUUID())
            .category(MedicationType.VACCINE) 
            .name("狂犬病ワクチン")
            .form("注射")
            .strength("1ml")
            .note("年1回接種")
            .build();

        Set<ConstraintViolation<Prescription>> violations = validator.validate(prescription);
        assertThat(violations).isEmpty();
        assertThat(prescription.getName()).isEqualTo("狂犬病ワクチン");
        assertThat(prescription.getCategory()).isEqualTo(MedicationType.VACCINE); 
    }

    @Test
    void shouldFailWhenCategoryIsNull() {
        Prescription prescription = Prescription.builder()
            .id(UUID.randomUUID())
            .category(null)
            .name("テスト薬")
            .build();

        Set<ConstraintViolation<Prescription>> violations = validator.validate(prescription);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("category"));
    }

    @Test
    void shouldFailWhenNameIsNull() {
        Prescription prescription = Prescription.builder()
            .id(UUID.randomUUID())
            .category(MedicationType.VACCINE) 
            .name(null)
            .build();

        Set<ConstraintViolation<Prescription>> violations = validator.validate(prescription);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    @Test
    void shouldFailWhenNameIsBlank() {
        Prescription prescription = Prescription.builder()
            .id(UUID.randomUUID())
            .category(MedicationType.HEARTWORM) 
            .name("")
            .build();

        Set<ConstraintViolation<Prescription>> violations = validator.validate(prescription);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    @Test
    void shouldAllowNullOptionalFields() {
        Prescription prescription = Prescription.builder()
            .id(UUID.randomUUID())
            .category(MedicationType.VACCINE) 
            .name("狂犬病ワクチン")
            .build();

        Set<ConstraintViolation<Prescription>> violations = validator.validate(prescription);
        assertThat(violations).isEmpty();
        assertThat(prescription.getForm()).isNull();
        assertThat(prescription.getStrength()).isNull();
        assertThat(prescription.getNote()).isNull();
    }

    @Test
    void shouldSupportAllPrescriptionCategories() {
        Prescription vaccine = Prescription.builder()
            .category(MedicationType.VACCINE) 
            .name("ワクチン")
            .build();
        assertThat(vaccine.getCategory()).isEqualTo(MedicationType.VACCINE); 

        Prescription heartworm = Prescription.builder()
            .category(MedicationType.HEARTWORM) 
            .name("フィラリア予防薬")
            .build();
        assertThat(heartworm.getCategory()).isEqualTo(MedicationType.HEARTWORM); 

        Prescription fleaTick = Prescription.builder()
            .category(MedicationType.FLEA_TICK) 
            .name("ノミ・ダニ駆除薬")
            .build();
        assertThat(fleaTick.getCategory()).isEqualTo(MedicationType.FLEA_TICK); 

        Prescription other = Prescription.builder()
            .category(MedicationType.OTHER) 
            .name("その他")
            .build();
        assertThat(other.getCategory()).isEqualTo(MedicationType.OTHER); 
    }
}