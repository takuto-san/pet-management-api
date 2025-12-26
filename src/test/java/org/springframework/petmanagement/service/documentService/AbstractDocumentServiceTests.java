package org.springframework.petmanagement.service.documentService;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.petmanagement.model.Document;
import org.springframework.petmanagement.service.DocumentService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractDocumentServiceTests {

    @Autowired
    protected DocumentService documentService;

    @Test
    void shouldFindAllBySpaceIdEvenIfEmpty() {
        assertThat(documentService.findAllBySpaceId(UUID.randomUUID())).isNotNull();
    }

    @Test
    void shouldCreateDocument() {
        Map<String, Object> body = Map.of("content", "Test content");
        Document document = Document.builder()
            .spaceId(UUID.randomUUID())
            .title("Test Document")
            .body(body)
            .build();

        Document saved = documentService.save(document);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Test Document");
    }

    @Test
    void shouldFindDocumentById() {
        Map<String, Object> body = Map.of("content", "Test content");
        Document document = Document.builder()
            .spaceId(UUID.randomUUID())
            .title("Test Document")
            .body(body)
            .build();

        Document saved = documentService.save(document);
        Document found = documentService.findById(saved.getId());
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(saved.getId());
    }
}
