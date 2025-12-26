package org.springframework.petmanagement.repository.springdatajpa;

import java.util.Collection;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.petmanagement.model.Document;
import org.springframework.petmanagement.repository.DocumentRepository;

@Profile("spring-data-jpa")
public interface SpringDataDocumentRepository extends DocumentRepository, CrudRepository<Document, UUID> {

    Collection<Document> findBySpaceId(UUID spaceId);

    Collection<Document> findByParentDocId(UUID parentDocId);
}
