package org.springframework.petmanagement.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.petmanagement.model.Item;
import org.springframework.petmanagement.rest.dto.ItemDto;
import org.springframework.petmanagement.rest.dto.ItemFieldsDto;

import java.util.List;

@Mapper(componentModel = "spring", uses = DateTimeMapper.class)
public interface ItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Item toItem(ItemFieldsDto itemFieldsDto);

    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    ItemDto toItemDto(Item item);

    List<ItemDto> toItemDtoList(List<Item> items);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateItemFromFields(ItemFieldsDto itemFieldsDto, @MappingTarget Item item);
}