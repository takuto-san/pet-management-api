package org.springframework.petmanagement.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.petmanagement.model.type.RoleType;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class RoleTests {

    private Validator validator;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        validator = localValidatorFactoryBean;
    }

    @Test
    void shouldCreateValidRole() {
        Role role = Role.builder()
            .id(UUID.randomUUID())
            .name(RoleType.OWNER)
            .build();

        Set<ConstraintViolation<Role>> violations = validator.validate(role);
        assertThat(violations).isEmpty();
        assertThat(role.getName()).isEqualTo(RoleType.OWNER);
    }

    @Test
    void shouldFailWhenNameIsNull() {
        Role role = Role.builder()
            .id(UUID.randomUUID())
            .name(null)
            .build();

        Set<ConstraintViolation<Role>> violations = validator.validate(role);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    @Test
    void shouldSupportBuilderPattern() {
        Role role = Role.builder()
            .name(RoleType.VET)
            .build();

        assertThat(role).isNotNull();
        assertThat(role.getName()).isEqualTo(RoleType.VET);
    }

    @Test
    void shouldSupportAllRoleTypes() {
        for (RoleType type : RoleType.values()) {
            Role role = Role.builder().name(type).build();
            assertThat(role.getName()).isEqualTo(type);
            
            Set<ConstraintViolation<Role>> violations = validator.validate(role);
            assertThat(violations).isEmpty();
        }
    }
}