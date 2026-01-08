package org.springframework.petmanagement.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;
import org.springframework.petmanagement.mapper.type.PetSexMapper;
import org.springframework.petmanagement.mapper.type.PetTypeMapper;
import org.springframework.petmanagement.model.Pet;
import org.springframework.petmanagement.rest.dto.PetDto;
import org.springframework.petmanagement.rest.dto.PetFieldsDto;
import org.springframework.petmanagement.rest.dto.PetPageDto;

@Mapper(
    componentModel = "spring",
    uses = { DateTimeMapper.class, PetTypeMapper.class, PetSexMapper.class }
)
public interface PetMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "sex", source = "sex")
    PetDto toPetDto(Pet pet);

    List<PetDto> toPetsDto(Collection<Pet> pets);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "type", source = "type")
    @Mapping(target = "sex", source = "sex")
    @Mapping(target = "icon", source = "icon")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Pet toPet(PetFieldsDto petFieldsDto);

    @Mapping(target = "content", expression = "java(toPetsDto(page.getContent()))")
    @Mapping(target = "size", source = "size")
    @Mapping(target = "totalElements", source = "totalElements")
    @Mapping(target = "totalPages", source = "totalPages")
    @Mapping(target = "number", source = "number")
    PetPageDto toPetPageDto(Page<Pet> page);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "type", source = "type")
    @Mapping(target = "sex", source = "sex")
    @Mapping(target = "icon", source = "icon")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updatePetFromFields(PetFieldsDto petFieldsDto, @MappingTarget Pet currentPet);
}
