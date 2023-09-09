package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

//мдау@Getter
//мдау@Setter
@Builder
@Data
//мдау@EqualsAndHashCode
public class BookingReferencedDto {

    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private int itemId;
    private int bookerId;
    private BookingStatus status;
}
