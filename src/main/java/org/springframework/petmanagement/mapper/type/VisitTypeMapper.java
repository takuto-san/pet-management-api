package org.springframework.petmanagement.mapper.type;

import org.mapstruct.Mapper;
import org.springframework.petmanagement.model.type.VisitType;
import org.springframework.petmanagement.rest.dto.VisitTypeDto;

@Mapper(componentModel = "spring")
public interface VisitTypeMapper {

    VisitType toVisitType(VisitTypeDto visitTypeDto);

    VisitTypeDto toVisitTypeDto(VisitType visitType);
}
