package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//мдау@Getter
//мдау@Setter
@Builder
//мдау@EqualsAndHashCode
//мдау@ToString
@Data
public class ResponseItemRequestDto {

    private int id;
    private String description;
    private List<ItemRequestDto> items = new ArrayList<>();
    private LocalDateTime created;
}
