package org.springframework.petmanagement.repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.petmanagement.model.Document;

public interface DocumentRepository {

    Document save(Document document) throws DataAccessException;

    void delete(Document document) throws DataAccessException;

    Optional<Document> findById(UUID id) throws DataAccessException;

    Collection<Document> findAll() throws DataAccessException;

    Collection<Document> findBySpaceId(UUID spaceId) throws DataAccessException;

    Collection<Document> findByParentDocId(UUID parentDocId) throws DataAccessException;
}
