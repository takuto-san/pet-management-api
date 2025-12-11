package org.springframework.petmanagement.model;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.petmanagement.model.type.VisitType;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

class VisitTests {

    private Validator validator;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        validator = localValidatorFactoryBean;
    }

    @Test
    void shouldCreateValidVisit() {
        User user = User.builder().id(UUID.randomUUID()).build();
        Pet pet = Pet.builder().id(UUID.randomUUID()).build();
        Clinic clinic = Clinic.builder().id(UUID.randomUUID()).build();

        Visit visit = Visit.builder()
            .id(UUID.randomUUID())
            .user(user)
            .pet(pet)
            .clinic(clinic)
            .visitedOn(LocalDate.of(2024, 1, 15))
            .weight(5.2f)
            .visitType(VisitType.CHECKUP)
            .reason("定期健診")
            .diagnosis("健康状態良好")
            .treatment("特になし")
            .nextDueOn(LocalDate.of(2024, 7, 15))
            .totalFee(5000)
            .currency("JPY")
            .note("次回予約あり")
            .build();

        Set<ConstraintViolation<Visit>> violations = validator.validate(visit);
        assertThat(violations).isEmpty();
        assertThat(visit.getVisitedOn()).isEqualTo(LocalDate.of(2024, 1, 15));
        assertThat(visit.getWeight()).isEqualTo(5.2f);
    }

    @Test
    void shouldFailWhenUserIsNull() {
        Pet pet = Pet.builder().id(UUID.randomUUID()).build();
        Clinic clinic = Clinic.builder().id(UUID.randomUUID()).build();

        Visit visit = Visit.builder()
            .id(UUID.randomUUID())
            .user(null)
            .pet(pet)
            .clinic(clinic)
            .visitedOn(LocalDate.now())
            .build();

        Set<ConstraintViolation<Visit>> violations = validator.validate(visit);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("user"));
    }

    @Test
    void shouldFailWhenPetIsNull() {
        User user = User.builder().id(UUID.randomUUID()).build();
        Clinic clinic = Clinic.builder().id(UUID.randomUUID()).build();

        Visit visit = Visit.builder()
            .id(UUID.randomUUID())
            .user(user)
            .pet(null)
            .clinic(clinic)
            .visitedOn(LocalDate.now())
            .build();

        Set<ConstraintViolation<Visit>> violations = validator.validate(visit);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("pet"));
    }

    @Test
    void shouldFailWhenClinicIsNull() {
        User user = User.builder().id(UUID.randomUUID()).build();
        Pet pet = Pet.builder().id(UUID.randomUUID()).build();

        Visit visit = Visit.builder()
            .id(UUID.randomUUID())
            .user(user)
            .pet(pet)
            .clinic(null)
            .visitedOn(LocalDate.now())
            .build();

        Set<ConstraintViolation<Visit>> violations = validator.validate(visit);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("clinic"));
    }

    @Test
    void shouldFailWhenVisitedOnIsNull() {
        User user = User.builder().id(UUID.randomUUID()).build();
        Pet pet = Pet.builder().id(UUID.randomUUID()).build();
        Clinic clinic = Clinic.builder().id(UUID.randomUUID()).build();

        Visit visit = Visit.builder()
            .id(UUID.randomUUID())
            .user(user)
            .pet(pet)
            .clinic(clinic)
            .visitedOn(null)
            .build();

        Set<ConstraintViolation<Visit>> violations = validator.validate(visit);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("visitedOn"));
    }

    @Test
    void shouldValidateCurrencyPattern() {
        User user = User.builder().id(UUID.randomUUID()).build();
        Pet pet = Pet.builder().id(UUID.randomUUID()).build();
        Clinic clinic = Clinic.builder().id(UUID.randomUUID()).build();

        Visit validVisit = Visit.builder()
            .user(user)
            .pet(pet)
            .clinic(clinic)
            .visitedOn(LocalDate.now())
            .currency("USD")
            .build();

        Set<ConstraintViolation<Visit>> violations = validator.validate(validVisit);
        assertThat(violations).isEmpty();

        Visit invalidVisit = Visit.builder()
            .user(user)
            .pet(pet)
            .clinic(clinic)
            .visitedOn(LocalDate.now())
            .currency("US")
            .build();

        violations = validator.validate(invalidVisit);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("currency"));
    }

    @Test
    void shouldAllowNullOptionalFields() {
        User user = User.builder().id(UUID.randomUUID()).build();
        Pet pet = Pet.builder().id(UUID.randomUUID()).build();
        Clinic clinic = Clinic.builder().id(UUID.randomUUID()).build();

        Visit visit = Visit.builder()
            .user(user)
            .pet(pet)
            .clinic(clinic)
            .visitedOn(LocalDate.now())
            .build();

        Set<ConstraintViolation<Visit>> violations = validator.validate(visit);
        assertThat(violations).isEmpty();
        assertThat(visit.getWeight()).isNull();
        assertThat(visit.getVisitType()).isNull();
        assertThat(visit.getReason()).isNull();
    }

    @Test
    void shouldSupportBuilderPattern() {
        User user = User.builder().id(UUID.randomUUID()).build();
        Pet pet = Pet.builder().id(UUID.randomUUID()).build();
        Clinic clinic = Clinic.builder().id(UUID.randomUUID()).build();

        Visit visit = Visit.builder()
            .user(user)
            .pet(pet)
            .clinic(clinic)
            .visitedOn(LocalDate.of(2024, 6, 1))
            .build();

        assertThat(visit).isNotNull();
        assertThat(visit.getVisitedOn()).isEqualTo(LocalDate.of(2024, 6, 1));
    }
}
