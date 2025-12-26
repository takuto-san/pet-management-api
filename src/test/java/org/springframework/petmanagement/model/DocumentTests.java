package org.springframework.petmanagement.model;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

class DocumentTests {

    private Validator validator;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        validator = localValidatorFactoryBean;
    }

    @Test
    void shouldCreateValidDocument() {
        UUID spaceId = UUID.randomUUID();
        UUID parentDocId = UUID.randomUUID();
        Map<String, Object> body = Map.of("content", "Test content");

        Document document = Document.builder()
            .id(UUID.randomUUID())
            .spaceId(spaceId)
            .parentDocId(parentDocId)
            .title("My Document")
            .body(body)
            .build();

        Set<ConstraintViolation<Document>> violations = validator.validate(document);
        assertThat(violations).isEmpty();
        assertThat(document.getTitle()).isEqualTo("My Document");
        assertThat(document.getSpaceId()).isEqualTo(spaceId);
        assertThat(document.getParentDocId()).isEqualTo(parentDocId);
        assertThat(document.getBody()).isEqualTo(body);
    }

    @Test
    void shouldFailWhenTitleIsNull() {
        Document document = Document.builder()
            .id(UUID.randomUUID())
            .spaceId(UUID.randomUUID())
            .title(null)
            .build();

        Set<ConstraintViolation<Document>> violations = validator.validate(document);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("title"));
    }

    @Test
    void shouldFailWhenTitleIsBlank() {
        Document document = Document.builder()
            .id(UUID.randomUUID())
            .spaceId(UUID.randomUUID())
            .title("")
            .build();

        Set<ConstraintViolation<Document>> violations = validator.validate(document);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("title"));
    }

    @Test
    void shouldFailWhenSpaceIdIsNull() {
        Document document = Document.builder()
            .id(UUID.randomUUID())
            .spaceId(null)
            .title("My Document")
            .build();

        Set<ConstraintViolation<Document>> violations = validator.validate(document);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("spaceId"));
    }

    @Test
    void shouldAllowNullParentDocId() {
        Document document = Document.builder()
            .id(UUID.randomUUID())
            .spaceId(UUID.randomUUID())
            .parentDocId(null)
            .title("Root Document")
            .build();

        Set<ConstraintViolation<Document>> violations = validator.validate(document);
        assertThat(violations).isEmpty();
        assertThat(document.getParentDocId()).isNull();
    }

    @Test
    void shouldAllowNullBody() {
        Document document = Document.builder()
            .id(UUID.randomUUID())
            .spaceId(UUID.randomUUID())
            .title("Document without body")
            .body(null)
            .build();

        Set<ConstraintViolation<Document>> violations = validator.validate(document);
        assertThat(violations).isEmpty();
        assertThat(document.getBody()).isNull();
    }

    @Test
    void shouldSupportBuilderPattern() {
        Document document = Document.builder()
            .title("Builder Document")
            .spaceId(UUID.randomUUID())
            .build();

        assertThat(document).isNotNull();
        assertThat(document.getTitle()).isEqualTo("Builder Document");
    }
}
