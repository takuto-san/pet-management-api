package org.springframework.petmanagement.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.petmanagement.model.base.Person;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

class ValidatorTests {

    private Validator createValidator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        return localValidatorFactoryBean;
    }

    @Test
    void shouldNotValidateWhenFirstNameEmpty() {

        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Person person = new Person();
        person.setFirstName("");
        person.setLastName("smith");
        
        person.setFirstNameKana("ジョージ");
        person.setLastNameKana("スミス");
        person.setEmail("test@example.com");

        Validator validator = createValidator();
        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        assertThat(constraintViolations.size()).isEqualTo(2);
        ConstraintViolation<Person> violation = constraintViolations.stream()
            .filter(v -> v.getPropertyPath().toString().equals("firstName"))
            .findFirst()
            .orElseThrow();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("firstName");
        assertThat(violation.getMessage()).isIn("must not be empty", "size must be between 1 and 30");
    }

}