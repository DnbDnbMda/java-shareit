package ru.practicum.shareit.item.dto;

import lombok.*;

@Builder
@Data
public class ItemRequestDto {

    private int id;
    private String name;
    private String description;
    private boolean available;
    private int requestId;
}
