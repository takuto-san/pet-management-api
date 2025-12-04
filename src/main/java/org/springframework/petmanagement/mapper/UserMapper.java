package org.springframework.petmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.petmanagement.model.Role;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.rest.dto.UserDto;
import org.springframework.petmanagement.rest.dto.UserFieldsDto; 

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = RoleMapper.class) 
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    User toUser(UserDto userDto);

    @Mapping(target = "password", ignore = true)
    UserDto toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    User toUser(UserFieldsDto userFieldsDto);

    default Set<Role> map(List<String> roleNames) {
        if (roleNames == null) {
            return null;
        }
        return roleNames.stream().map(this::mapRoleName)
            .collect(Collectors.toSet());
    }

    default Role mapRoleName(String roleName) {
        if (roleName == null) {
            return null;
        }
        Role role = new Role();
        role.setRole(roleName); 
        return role;
    }
}