package org.springframework.petmanagement.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;
import org.springframework.petmanagement.model.UserItem;
import org.springframework.petmanagement.rest.dto.UserItemDto;
import org.springframework.petmanagement.rest.dto.UserItemFieldsDto;
import org.springframework.petmanagement.rest.dto.UserItemPageDto;

@Mapper(componentModel = "spring", uses = DateTimeMapper.class)
public interface UserItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "recordedAt", ignore = true)
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "item.id", source = "itemId")
    UserItem toUserItem(UserItemFieldsDto userItemFieldsDto);

    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "userId", expression = "java(userItem.getUser() != null ? userItem.getUser().getId() : null)")
    @Mapping(target = "itemId", expression = "java(userItem.getItem() != null ? userItem.getItem().getId() : null)")
    UserItemDto toUserItemDto(UserItem userItem);

    List<UserItemDto> toUserItemDtoList(List<UserItem> userItems);

    @Mapping(target = "content", expression = "java(toUserItemDtoList(page.getContent()))")
    @Mapping(target = "size", source = "size")
    @Mapping(target = "totalElements", source = "totalElements")
    @Mapping(target = "totalPages", source = "totalPages")
    @Mapping(target = "number", source = "number")
    UserItemPageDto toUserItemPageDto(Page<UserItem> page);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "recordedAt", ignore = true)
    void updateUserItemFromFields(UserItemFieldsDto userItemFieldsDto, @MappingTarget UserItem userItem);
}
