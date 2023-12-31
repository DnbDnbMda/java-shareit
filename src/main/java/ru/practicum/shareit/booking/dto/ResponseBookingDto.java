package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.PostItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Builder
@Data
public class ResponseBookingDto {

    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private PostItemDto item;
    private UserDto booker;
    private BookingStatus status;
}
