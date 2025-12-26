package org.springframework.petmanagement.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.petmanagement.model.Document;
import org.springframework.petmanagement.repository.DocumentRepository;
import org.springframework.petmanagement.service.DocumentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Document> findAllBySpaceId(UUID spaceId) {
        return documentRepository.findBySpaceId(spaceId).stream().toList();
    }

    @Override
    public Document save(Document document) {
        return documentRepository.save(document);
    }

    @Override
    @Transactional(readOnly = true)
    public Document findById(UUID id) {
        return documentRepository.findById(id).orElseThrow(() -> new DataAccessException("Document not found") {
        });
    }

    @Override
    public Document update(Document document) {
        return documentRepository.save(document);
    }

    @Override
    public void deleteById(UUID id) {
        documentRepository.delete(findById(id));
    }
}
