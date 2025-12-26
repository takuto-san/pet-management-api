package org.springframework.petmanagement.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;
import org.springframework.petmanagement.model.Item;
import org.springframework.petmanagement.rest.dto.ItemDto;
import org.springframework.petmanagement.rest.dto.ItemFieldsDto;
import org.springframework.petmanagement.rest.dto.ItemPageDto;

@Mapper(componentModel = "spring", uses = DateTimeMapper.class)
public interface ItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Item toItem(ItemFieldsDto itemFieldsDto);

    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "userId", expression = "java(item.getUser() != null ? item.getUser().getId() : null)")
    ItemDto toItemDto(Item item);

    List<ItemDto> toItemDtoList(List<Item> items);

    @Mapping(target = "content", expression = "java(toItemDtoList(page.getContent()))")
    @Mapping(target = "size", source = "size")
    @Mapping(target = "totalElements", source = "totalElements")
    @Mapping(target = "totalPages", source = "totalPages")
    @Mapping(target = "number", source = "number")
    ItemPageDto toItemPageDto(Page<Item> page);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateItemFromFields(ItemFieldsDto itemFieldsDto, @MappingTarget Item item);
}
