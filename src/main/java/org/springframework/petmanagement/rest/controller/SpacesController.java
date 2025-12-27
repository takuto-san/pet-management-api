package org.springframework.petmanagement.rest.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.petmanagement.mapper.DocumentMapper;
import org.springframework.petmanagement.mapper.SpaceMapper;
import org.springframework.petmanagement.model.Document;
import org.springframework.petmanagement.model.Space;
import org.springframework.petmanagement.rest.api.SpacesApi;
import org.springframework.petmanagement.rest.dto.DocumentDto;
import org.springframework.petmanagement.rest.dto.DocumentFieldsDto;
import org.springframework.petmanagement.rest.dto.DocumentUpdateFieldsDto;
import org.springframework.petmanagement.rest.dto.SpaceDto;
import org.springframework.petmanagement.rest.dto.SpaceFieldsDto;
import org.springframework.petmanagement.service.DocumentService;
import org.springframework.petmanagement.service.SpaceService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
public class SpacesController implements SpacesApi {

    private final SpaceService spaceService;
    private final DocumentService documentService;
    private final SpaceMapper spaceMapper;
    private final DocumentMapper documentMapper;

    public SpacesController(SpaceService spaceService, DocumentService documentService,
                            SpaceMapper spaceMapper, DocumentMapper documentMapper) {
        this.spaceService = spaceService;
        this.documentService = documentService;
        this.spaceMapper = spaceMapper;
        this.documentMapper = documentMapper;
    }

    @Override
    public ResponseEntity<List<SpaceDto>> listSpaces() {
        UUID userId = getCurrentUserId();
        List<Space> spaces = spaceService.findAllByUserId(userId);
        List<SpaceDto> dtoList = spaces.stream()
            .map(spaceMapper::toSpaceDto)
            .collect(Collectors.toList());
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SpaceDto> addSpace(SpaceFieldsDto spaceFieldsDto) {
        UUID userId = getCurrentUserId();
        Space space = Space.builder()
            .userId(userId)
            .name(spaceFieldsDto.getName())
            .build();
        Space created = spaceService.save(space);
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(spaceMapper.toSpaceDto(created), headers, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<DocumentDto>> listDocuments(UUID spaceId) {
        List<Document> documents = documentService.findAllBySpaceId(spaceId);
        List<DocumentDto> dtoList = documents.stream()
            .map(documentMapper::toDocumentDto)
            .collect(Collectors.toList());
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DocumentDto> addDocument(UUID spaceId, DocumentFieldsDto documentFieldsDto) {
        Document document = Document.builder()
            .spaceId(spaceId)
            .parentDocId(documentFieldsDto.getParentDocId())
            .title(documentFieldsDto.getTitle())
            .body(documentFieldsDto.getBody())
            .build();
        Document created = documentService.save(document);
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(documentMapper.toDocumentDto(created), headers, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<DocumentDto> getDocument(UUID spaceId, UUID documentId) {
        Document document = documentService.findById(documentId);
        return new ResponseEntity<>(documentMapper.toDocumentDto(document), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DocumentDto> updateDocument(UUID spaceId, UUID documentId, DocumentUpdateFieldsDto documentUpdateFieldsDto) {
        Document existing = documentService.findById(documentId);
        if (documentUpdateFieldsDto.getTitle() != null) {
            existing.setTitle(documentUpdateFieldsDto.getTitle());
        }
        if (documentUpdateFieldsDto.getBody() != null) {
            existing.setBody(documentUpdateFieldsDto.getBody());
        }
        Document updated = documentService.update(existing);
        return new ResponseEntity<>(documentMapper.toDocumentDto(updated), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteDocument(UUID spaceId, UUID documentId) {
        documentService.deleteById(documentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Assuming the principal is a UserDetails or similar with getId method
        // Adjust based on your security setup
        if (authentication.getPrincipal() instanceof org.springframework.petmanagement.model.User user) {
            return user.getId();
        }
        // For testing purposes, return a default UUID
        return UUID.fromString("00000000-0000-0000-0000-000000000000");
    }
}
