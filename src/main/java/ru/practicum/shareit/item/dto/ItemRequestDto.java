package ru.practicum.shareit.item.dto;

import lombok.*;

@Builder
//мдау@Getter
//мдау@Setter
//мдау@EqualsAndHashCode
//мдау@ToString
@Data
public class ItemRequestDto {

    private int id;
    private String name;
    private String description;
    private boolean available;
    private int requestId;
}
