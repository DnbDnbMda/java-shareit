package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.PostItemDto;
import ru.practicum.shareit.item.dto.ResponseCommentDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.ShareitPageRequest;
import ru.practicum.shareit.validation.ValidationErrors;
import ru.practicum.shareit.validation.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.shareit.utils.Sorts.SORT_BY_START;
import static ru.practicum.shareit.utils.Sorts.SORT_BY_START_DESC;
import static ru.practicum.shareit.validation.ValidationErrors.INVALID_USER_ID;
import static ru.practicum.shareit.validation.ValidationErrors.RESOURCE_NOT_FOUND;

@RequiredArgsConstructor
@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    private static final Comparator<Booking> BOOKING_COMPARATOR = Comparator.comparing(Booking::getStart);

    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public Item addItem(PostItemDto postItemDto, int userId) {
        User user = findUser(userId);
        Item item = ItemMapper.toItem(postItemDto);
        item.setOwner(user);
        ItemRequest request = null;
        if (postItemDto.getRequestId() != null) {
            request = itemRequestRepository.findById(postItemDto.getRequestId()).orElse(null);
        }
        item.setItemRequest(request);
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(Item updateItem, int ownerId) {
        User owner = findUser(ownerId);
        updateItem.setOwner(owner);

        Item item = getItem(updateItem.getId());
        if (updateItem.getName() != null && !updateItem.getName().isBlank()) {
            item.setName(updateItem.getName());
        }
        if (updateItem.getDescription() != null && !updateItem.getDescription().isBlank()) {
            item.setDescription(updateItem.getDescription());
        }
        if (updateItem.getAvailable() != null) {
            item.setAvailable(updateItem.getAvailable());
        }
        return item;
    }

    @Transactional(readOnly = true)
    public Item getItem(int itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseItemDto getItemForUser(int itemId, int userId) {
        Item item = getItem(itemId);
        List<Comment> comments = commentRepository.findByItemId(itemId);
        LocalDateTime now = LocalDateTime.now();
        Booking lastBooking = null;
        Booking nextBooking = null;
        if (item.getOwner().getId() == userId) {
            lastBooking = bookingRepository.findBookingByItemIdAndStartBefore(item.getId(), now, SORT_BY_START_DESC).stream().findFirst().orElse(null);
            nextBooking = bookingRepository.findBookingByItemIdAndStartAfterAndStatus(item.getId(), now, BookingStatus.APPROVED, SORT_BY_START).stream().findFirst().orElse(null);
        }
        return ItemMapper.toResponseItemDto(item, lastBooking, nextBooking, comments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseItemDto> getAll(int userId, int from, int size) {
        User owner = findUser(userId);
        Pageable page = new ShareitPageRequest(from, size);
        Collection<Item> items = itemRepository.findAllByOwnerOrderById(owner, page).toList();
        return toResponseItemDto(items);
    }

    private List<ResponseItemDto> toResponseItemDto(Collection<Item> items) {
        Map<Item, List<Booking>> bookingsByItem = findApprovedBookingsByItem(items);

        Map<Item, List<Comment>> comments = findComments(items);
        LocalDateTime now = LocalDateTime.now();
        return items.stream()
                .map(item -> getResponseItemDto(item, bookingsByItem.get(item), comments.get(item), now))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    private Map<Item, List<Comment>> findComments(Collection<Item> items) {
        return commentRepository.findByItemIn(items).stream().collect(Collectors.groupingBy(Comment::getItem));
    }

    @Transactional(readOnly = true)
    private Map<Item, List<Booking>> findApprovedBookingsByItem(Collection<Item> items) {
        return bookingRepository.findBookingByItemInAndStatus(items, BookingStatus.APPROVED).stream()
                .collect(Collectors.groupingBy(Booking::getItem, Collectors.toList()));
    }

    @Transactional(readOnly = true)
    private ResponseItemDto getResponseItemDto(Item item, List<Booking> bookings, List<Comment> comments, LocalDateTime now) {
        Booking lastBooking = null;
        Booking nextBooking = null;
        if (bookings != null && !bookings.isEmpty()) {
            lastBooking = bookings.stream().sorted(BOOKING_COMPARATOR)
                    .filter(booking -> booking.getStart().isBefore(now))
                    .findFirst().orElse(null);
            nextBooking = bookings.stream().sorted(BOOKING_COMPARATOR)
                    .filter(booking -> booking.getStart().isAfter(now))
                    .findFirst().orElse(null);
        }
        return ItemMapper.toResponseItemDto(item, lastBooking, nextBooking, comments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseItemDto> findItemsByText(String text, int from, int size) {
        if (text == null || text.isBlank()) {
            return Collections.EMPTY_LIST;
        }
        Pageable page = new ShareitPageRequest(from, size);
        List<Item> items = itemRepository.search(text, page);
        return toResponseItemDto(items);
    }

    @Override
    public ResponseCommentDto createComment(CommentDto commentDto, int itemId, int userId) {
        Item item = getItem(itemId);
        User author = findUser(userId);
        Collection<Booking> bookings = bookingRepository.findBookingByItemIdAndBookerIdAndStatusAndStartBefore(itemId, userId, BookingStatus.APPROVED, LocalDateTime.now());
        if (bookings == null || bookings.isEmpty()) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, INVALID_USER_ID);
        }
        Comment comment = CommentMapper.toComment(commentDto, item, author, LocalDateTime.now());
        comment = commentRepository.save(comment);
        return CommentMapper.toResponseCommentDto(comment);
    }

    private User findUser(int userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND, ValidationErrors.RESOURCE_NOT_FOUND));
    }
}
