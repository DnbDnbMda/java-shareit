package ru.practicum.shareit.item.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
//мдау@EqualsAndHashCode
//мдау@Getter
//мдау@ToString
@Data
public class ResponseCommentDto {

    private int id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
