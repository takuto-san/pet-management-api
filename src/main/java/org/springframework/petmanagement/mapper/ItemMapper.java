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

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "id", ignore = true)
    Item toItem(ItemFieldsDto itemFieldsDto);

    ItemDto toItemDto(Item item);

    List<ItemDto> toItemDtoList(List<Item> items);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateItemFromFields(ItemFieldsDto itemFieldsDto, @MappingTarget Item item);
}
