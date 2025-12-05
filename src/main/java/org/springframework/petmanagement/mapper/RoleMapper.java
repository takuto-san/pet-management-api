package org.springframework.petmanagement.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.petmanagement.model.Role;
import org.springframework.petmanagement.rest.dto.RoleDto;
import java.util.Collection;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Role toRole(RoleDto roleDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRoleFromDto(RoleDto roleDto, @MappingTarget Role currentRole);

    RoleDto toRoleDto(Role source);

    Collection<RoleDto> toRoleDtos(Collection<Role> roles);

    Collection<Role> toRoles(Collection<RoleDto> roleDtos);

}