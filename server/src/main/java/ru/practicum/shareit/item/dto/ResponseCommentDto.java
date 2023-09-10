package ru.practicum.shareit.item.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Data
public class ResponseCommentDto {

    private int id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
