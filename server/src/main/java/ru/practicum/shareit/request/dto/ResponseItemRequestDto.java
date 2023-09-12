package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
public class ResponseItemRequestDto {

    private int id;
    private String description;
    private List<ItemRequestDto> items = new ArrayList<>();
    private LocalDateTime created;
}
