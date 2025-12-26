package org.springframework.petmanagement.service;

import java.util.List;
import java.util.UUID;

import org.springframework.petmanagement.model.Document;

public interface DocumentService {

    List<Document> findAllBySpaceId(UUID spaceId);

    Document save(Document document);

    Document findById(UUID id);

    Document update(Document document);

    void deleteById(UUID id);
}
