package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingReferencedDto;

import java.util.List;

@AllArgsConstructor
//мдау@Getter
//мдау@Setter
@Builder
//мдау@EqualsAndHashCode
//мдау@ToString
@Data
public class ResponseItemDto {

    private int id;
    private String name;
    private String description;
    private Boolean available;
    private BookingReferencedDto lastBooking;
    private BookingReferencedDto nextBooking;
    private List<ResponseCommentDto> comments;
    private Integer requestId;
}
