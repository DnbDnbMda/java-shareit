package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostBookingDto {

    private int itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}
