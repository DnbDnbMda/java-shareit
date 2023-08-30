package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.UserForResponseDto;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface UserForResponseMapper {
    /**
     * Из объекта для ответа в контроллере в юзера.
     */
    User mapToModel(UserForResponseDto userForResponseDto);

    /**
     * Из юзера в объект для ответа в контроллере.
     */
    UserForResponseDto mapToDto(User user);
}
