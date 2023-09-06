package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    /**
     * Из объекта для ответа в контроллере в юзера.
     */
    Item mapToModel(ItemDto itemDto);

    /**
     * Из юзера в объект для ответа в контроллере.
     */
    ItemDto mapToDto(Item item);
}
