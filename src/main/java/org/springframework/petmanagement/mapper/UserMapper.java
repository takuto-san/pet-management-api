package org.springframework.petmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.petmanagement.model.Role;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.rest.dto.RoleDto;
import org.springframework.petmanagement.rest.dto.UserDto;

import java.util.Collection;

/**
 * Map User/Role & UserDto/RoleDto using mapstruct
 */
@Mapper
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Role toRole(RoleDto roleDto);

    RoleDto toRoleDto(Role role);

    Collection<RoleDto> toRoleDtos(Collection<Role> roles);

    User toUser(UserDto userDto);

    UserDto toUserDto(User user);

    Collection<Role> toRoles(Collection<RoleDto> roleDtos);

}
