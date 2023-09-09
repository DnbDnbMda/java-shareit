package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.PostItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Builder
//мдау@Setter
//мдау@Getter
@Data
public class ResponseBookingDto {

    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private PostItemDto item;
    private UserDto booker;
    private BookingStatus status;
}
