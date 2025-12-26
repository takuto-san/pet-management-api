package org.springframework.petmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.petmanagement.model.Space;
import org.springframework.petmanagement.rest.dto.SpaceDto;

@Mapper(componentModel = "spring", uses = {DateTimeMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SpaceMapper {

    SpaceDto toSpaceDto(Space space);
}
