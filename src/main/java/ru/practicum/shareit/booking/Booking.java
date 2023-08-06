package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.Getter;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Getter
public class Booking {
    private Long id;                    //ID бронирования
    private Long itemId;                //ID вещи
    private User booker;
    private LocalDateTime startTime;    //Дата начала бронирования
    private LocalDateTime endTime;      //Дата окончания бронирования
}
