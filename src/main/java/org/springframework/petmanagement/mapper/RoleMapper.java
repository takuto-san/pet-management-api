package org.springframework.petmanagement.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.petmanagement.model.Role;
import org.springframework.petmanagement.rest.dto.RoleDto;

import java.util.List;

@Mapper(componentModel = "spring") 
public interface RoleMapper {

    @Mapping(target = "id", ignore = true)
    Role toRole(RoleDto roleDto);

    RoleDto toRoleDto(Role role);

    List<RoleDto> toRoleDtoList(List<Role> roles);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateRoleFromDto(RoleDto roleDto, @MappingTarget Role role);
}