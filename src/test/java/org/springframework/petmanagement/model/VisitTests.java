package org.springframework.petmanagement.model;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.petmanagement.model.type.Currency;
import org.springframework.petmanagement.model.type.PetType;
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
        User user = User.builder().id(UUID.randomUUID()).username("test").email("test@example.com").firstName("Test").lastName("User").build();
        Pet pet = Pet.builder().id(UUID.randomUUID()).name("ポチ").type(PetType.DOG).user(user).build();
        Clinic clinic = Clinic.builder().id(UUID.randomUUID()).name("Clinic").build();

        Visit visit = Visit.builder()
            .id(UUID.randomUUID())
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
            .currency(Currency.JPY)
            .note("次回予約あり")
            .build();

        Set<ConstraintViolation<Visit>> violations = validator.validate(visit);
        assertThat(violations).isEmpty();
        assertThat(visit.getVisitedOn()).isEqualTo(LocalDate.of(2024, 1, 15));
        assertThat(visit.getWeight()).isEqualTo(5.2f);
    }

    @Test
    void shouldFailWhenPetIsNull() {
        Clinic clinic = Clinic.builder().id(UUID.randomUUID()).name("Clinic").build();

        Visit visit = Visit.builder()
            .id(UUID.randomUUID())
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
        User user = User.builder().id(UUID.randomUUID()).username("test").email("test@example.com").firstName("Test").lastName("User").build();
        Pet pet = Pet.builder().id(UUID.randomUUID()).name("ポチ").type(PetType.DOG).user(user).build();

        Visit visit = Visit.builder()
            .id(UUID.randomUUID())
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
        User user = User.builder().id(UUID.randomUUID()).username("test").email("test@example.com").firstName("Test").lastName("User").build();
        Pet pet = Pet.builder().id(UUID.randomUUID()).name("ポチ").type(PetType.DOG).user(user).build();
        Clinic clinic = Clinic.builder().id(UUID.randomUUID()).name("Clinic").build();

        Visit visit = Visit.builder()
            .id(UUID.randomUUID())
            .pet(pet)
            .clinic(clinic)
            .visitedOn(null)
            .build();

        Set<ConstraintViolation<Visit>> violations = validator.validate(visit);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("visitedOn"));
    }

    @Test
    void shouldAllowNullOptionalFields() {
        User user = User.builder().id(UUID.randomUUID()).username("test").email("test@example.com").firstName("Test").lastName("User").build();
        Pet pet = Pet.builder().id(UUID.randomUUID()).name("ポチ").type(PetType.DOG).user(user).build();
        Clinic clinic = Clinic.builder().id(UUID.randomUUID()).name("Clinic").build();

        Visit visit = Visit.builder()
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
        User user = User.builder().id(UUID.randomUUID()).username("test").email("test@example.com").firstName("Test").lastName("User").build();
        Pet pet = Pet.builder().id(UUID.randomUUID()).name("ポチ").type(PetType.DOG).user(user).build();
        Clinic clinic = Clinic.builder().id(UUID.randomUUID()).name("Clinic").build();

        Visit visit = Visit.builder()
            .pet(pet)
            .clinic(clinic)
            .visitedOn(LocalDate.of(2024, 6, 1))
            .build();

        assertThat(visit).isNotNull();
        assertThat(visit.getVisitedOn()).isEqualTo(LocalDate.of(2024, 6, 1));
    }
}