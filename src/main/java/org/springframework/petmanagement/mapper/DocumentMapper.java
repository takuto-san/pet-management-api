package org.springframework.petmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.petmanagement.model.Document;
import org.springframework.petmanagement.rest.dto.DocumentDto;

@Mapper(componentModel = "spring", uses = {DateTimeMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DocumentMapper {

    DocumentDto toDocumentDto(Document document);
}
