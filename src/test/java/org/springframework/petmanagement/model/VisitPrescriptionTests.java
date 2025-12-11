package org.springframework.petmanagement.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class VisitPrescriptionTests {

    private Validator validator;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        validator = localValidatorFactoryBean;
    }

    @Test
    void shouldCreateValidVisitPrescription() {
        Visit visit = Visit.builder().id(UUID.randomUUID()).build();
        Prescription prescription = Prescription.builder().id(UUID.randomUUID()).build();

        VisitPrescription visitPrescription = VisitPrescription.builder()
            .id(UUID.randomUUID())
            .visit(visit)
            .prescription(prescription)
            .quantity(2.5f)
            .unit("錠")
            .days(7)
            .dosageInstructions("1日2回、食後に服用")
            .purpose("感染症治療")
            .build();

        Set<ConstraintViolation<VisitPrescription>> violations = validator.validate(visitPrescription);
        assertThat(violations).isEmpty();
        assertThat(visitPrescription.getQuantity()).isEqualTo(2.5f);
        assertThat(visitPrescription.getUnit()).isEqualTo("錠");
    }

    @Test
    void shouldFailWhenVisitIsNull() {
        Prescription prescription = Prescription.builder().id(UUID.randomUUID()).build();

        VisitPrescription visitPrescription = VisitPrescription.builder()
            .id(UUID.randomUUID())
            .visit(null)
            .prescription(prescription)
            .quantity(1.0f)
            .unit("ml")
            .build();

        Set<ConstraintViolation<VisitPrescription>> violations = validator.validate(visitPrescription);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("visit"));
    }

    @Test
    void shouldFailWhenPrescriptionIsNull() {
        Visit visit = Visit.builder().id(UUID.randomUUID()).build();

        VisitPrescription visitPrescription = VisitPrescription.builder()
            .id(UUID.randomUUID())
            .visit(visit)
            .prescription(null)
            .quantity(1.0f)
            .unit("ml")
            .build();

        Set<ConstraintViolation<VisitPrescription>> violations = validator.validate(visitPrescription);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("prescription"));
    }

    @Test
    void shouldFailWhenQuantityIsNull() {
        Visit visit = Visit.builder().id(UUID.randomUUID()).build();
        Prescription prescription = Prescription.builder().id(UUID.randomUUID()).build();

        VisitPrescription visitPrescription = VisitPrescription.builder()
            .id(UUID.randomUUID())
            .visit(visit)
            .prescription(prescription)
            .quantity(null)
            .unit("錠")
            .build();

        Set<ConstraintViolation<VisitPrescription>> violations = validator.validate(visitPrescription);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("quantity"));
    }

    @Test
    void shouldFailWhenQuantityIsNotPositive() {
        Visit visit = Visit.builder().id(UUID.randomUUID()).build();
        Prescription prescription = Prescription.builder().id(UUID.randomUUID()).build();

        VisitPrescription visitPrescription = VisitPrescription.builder()
            .id(UUID.randomUUID())
            .visit(visit)
            .prescription(prescription)
            .quantity(0.0f)
            .unit("錠")
            .build();

        Set<ConstraintViolation<VisitPrescription>> violations = validator.validate(visitPrescription);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("quantity"));
    }

    @Test
    void shouldFailWhenUnitIsNull() {
        Visit visit = Visit.builder().id(UUID.randomUUID()).build();
        Prescription prescription = Prescription.builder().id(UUID.randomUUID()).build();

        VisitPrescription visitPrescription = VisitPrescription.builder()
            .id(UUID.randomUUID())
            .visit(visit)
            .prescription(prescription)
            .quantity(1.0f)
            .unit(null)
            .build();

        Set<ConstraintViolation<VisitPrescription>> violations = validator.validate(visitPrescription);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("unit"));
    }

    @Test
    void shouldFailWhenUnitIsBlank() {
        Visit visit = Visit.builder().id(UUID.randomUUID()).build();
        Prescription prescription = Prescription.builder().id(UUID.randomUUID()).build();

        VisitPrescription visitPrescription = VisitPrescription.builder()
            .id(UUID.randomUUID())
            .visit(visit)
            .prescription(prescription)
            .quantity(1.0f)
            .unit("")
            .build();

        Set<ConstraintViolation<VisitPrescription>> violations = validator.validate(visitPrescription);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("unit"));
    }

    @Test
    void shouldFailWhenDaysIsLessThanOne() {
        Visit visit = Visit.builder().id(UUID.randomUUID()).build();
        Prescription prescription = Prescription.builder().id(UUID.randomUUID()).build();

        VisitPrescription visitPrescription = VisitPrescription.builder()
            .id(UUID.randomUUID())
            .visit(visit)
            .prescription(prescription)
            .quantity(1.0f)
            .unit("錠")
            .days(0)
            .build();

        Set<ConstraintViolation<VisitPrescription>> violations = validator.validate(visitPrescription);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("days"));
    }

    @Test
    void shouldAllowNullOptionalFields() {
        Visit visit = Visit.builder().id(UUID.randomUUID()).build();
        Prescription prescription = Prescription.builder().id(UUID.randomUUID()).build();

        VisitPrescription visitPrescription = VisitPrescription.builder()
            .id(UUID.randomUUID())
            .visit(visit)
            .prescription(prescription)
            .quantity(1.0f)
            .unit("錠")
            .build();

        Set<ConstraintViolation<VisitPrescription>> violations = validator.validate(visitPrescription);
        assertThat(violations).isEmpty();
        assertThat(visitPrescription.getDays()).isNull();
        assertThat(visitPrescription.getDosageInstructions()).isNull();
        assertThat(visitPrescription.getPurpose()).isNull();
    }

    @Test
    void shouldSupportBuilderPattern() {
        Visit visit = Visit.builder().id(UUID.randomUUID()).build();
        Prescription prescription = Prescription.builder().id(UUID.randomUUID()).build();

        VisitPrescription visitPrescription = VisitPrescription.builder()
            .visit(visit)
            .prescription(prescription)
            .quantity(3.0f)
            .unit("包")
            .build();

        assertThat(visitPrescription).isNotNull();
        assertThat(visitPrescription.getQuantity()).isEqualTo(3.0f);
    }
}
