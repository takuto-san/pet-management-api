package org.springframework.petmanagement.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.rest.dto.AdminUserUpdateDto;
import org.springframework.petmanagement.rest.dto.UserBaseDto;
import org.springframework.petmanagement.rest.dto.UserRegistrationDto;
import org.springframework.petmanagement.rest.dto.UserResponseDto;

@Mapper(componentModel = "spring", uses = {DateTimeMapper.class, RoleMapper.class})
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "pets", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toUserFromDto(UserResponseDto userDto);

    UserResponseDto toUserDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "pets", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toUser(UserRegistrationDto userFieldsDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "pets", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateUserFromFields(AdminUserUpdateDto userFieldsDto, @MappingTarget User currentUser);
}
