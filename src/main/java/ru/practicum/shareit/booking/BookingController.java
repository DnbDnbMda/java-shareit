package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.PostBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.utils.Messages;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    ResponseBookingDto add(@Valid @RequestBody PostBookingDto postBookingDto,
                           @RequestHeader("${useridheader}") int bookerId) {
        log.info(Messages.addBooking());
        Booking booking = bookingService.addBooking(postBookingDto, bookerId);
        return BookingMapper.toResponseBookingDto(booking);
    }

    @PatchMapping("/{bookingId}")
    ResponseBookingDto approve(@PathVariable int bookingId, @RequestParam boolean approved,
                               @RequestHeader("${useridheader}") int userId) {
        log.info(Messages.approveBooking(bookingId, userId, approved));
        Booking booking = bookingService.approve(bookingId, approved, userId);
        return BookingMapper.toResponseBookingDto(booking);
    }

    @GetMapping("/{bookingId}")
    public ResponseBookingDto getById(@PathVariable int bookingId,
                                      @RequestHeader("${useridheader}") int userId) {
        log.info(Messages.findBooking(bookingId, userId));
        Booking booking = bookingService.getBookingForUser(bookingId, userId);
        return BookingMapper.toResponseBookingDto(booking);
    }

    @GetMapping
    public Collection<ResponseBookingDto> getAllBookings(
            @RequestParam(value = "state", defaultValue = "ALL") BookingState state,
                                                         @RequestHeader("${useridheader}") int userId,
                                                         @RequestParam(defaultValue = "0")
                                                         @PositiveOrZero int from,
                                                         @RequestParam(defaultValue = "20")
                                                         @Positive int size) {
        log.info(Messages.findAllBookings(state, userId));
        List<Booking> bookings = bookingService.getAllBookings(state, userId, from, size);
        return BookingMapper.toBookingReferencedDto(bookings);
    }

    @GetMapping("/owner")
    public Collection<ResponseBookingDto> getAllBookingForOwner(
            @RequestParam(value = "state", defaultValue = "ALL") BookingState state,
                                                                @RequestHeader("${useridheader}") int ownerId,
                                                                @RequestParam(defaultValue = "0")
                                                                @PositiveOrZero int from,
                                                                @RequestParam(defaultValue = "20")
                                                                @Positive int size) {
        log.info(Messages.findAllBookingsForOwner(ownerId, state));
        Collection<Booking> bookings = bookingService.getAllBookingForOwner(state, ownerId, from, size);
        return BookingMapper.toBookingReferencedDto(bookings);
    }
}
