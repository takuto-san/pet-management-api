package org.springframework.petmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.petmanagement.model.Role;
import org.springframework.petmanagement.rest.dto.RoleDto;
import java.util.Collection;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Role toRole(RoleDto roleDto);

    RoleDto toRoleDto(Role source);

    Collection<RoleDto> toRoleDtos(Collection<Role> roles);
    Collection<Role> toRoles(Collection<RoleDto> roleDtos);

}