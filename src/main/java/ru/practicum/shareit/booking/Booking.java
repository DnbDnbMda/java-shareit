package ru.practicum.shareit.booking;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    private Long id;                    //ID бронирования
    private Long itemId;                //ID вещи
    private LocalDateTime startTime;    //Дата начала бронирования
    private LocalDateTime endTime;      //Дата окончания бронирования
}
