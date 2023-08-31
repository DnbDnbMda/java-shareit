package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingAndCommentsDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemWithBookingAndCommentsDto getItemByIdForOwner(@RequestHeader("${defaultHeader}") Long ownerId,
                                                             @PathVariable Long itemId) {
        ItemWithBookingAndCommentsDto dto = itemService.getItemWithBookingAndComment(itemId, ownerId);
        log.debug("Отправлен ответ: " + dto.toString());
        return dto;
    }

    @GetMapping()
    public List<ItemWithBookingAndCommentsDto> getAll(@RequestHeader("${defaultHeader}") Long userId) {
        List<ItemWithBookingAndCommentsDto> dto = itemService.getItemsByUserId(userId);
        log.info("Отправлен ответ" + dto.toString());
        return dto;
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItemsByText(@RequestParam(value = "text", required = false) String text) {
        return itemService.searchItemsByText(text);
    }

    /**
     * Добавление новой вещи. Будет происходить по эндофиту POST /items. На вход поступает объект ItemDto. userId в
     * заголовке X-Sharer-User-Id — это идентификатор пользователя, который добавляет вещь. Именно этот пользователь —
     * владелец вещи. Идентификатор владельца будет поступать на вход в каждом из запросов, рассмотренных далее.
     *
     * @return добавленная в БД вещь.
     */
    @PostMapping
    public ItemDto add(@RequestHeader(value = "${defaultHeader}", required = false) Long ownerId,
                       @RequestBody @Validated ItemDto itemDto) {
        ItemDto dto = itemService.add(itemDto, ownerId);
        log.info("Отправлен ответ" + dto.toString());
        return dto;
    }

    @PatchMapping("{itemId}")
    public ItemDto update(@RequestHeader(value = "${defaultHeader}", required = false) Long ownerId,
                          @PathVariable Long itemId, @Validated @RequestBody ItemDto itemDto) {
        System.out.println(" - Обновление вещи с ID = " + itemId + " юзера с ID = " + ownerId + ".");
        return itemService.updateInStorage(itemId, itemDto, ownerId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addCommentToItem(@RequestHeader("${defaultHeader}") Long userId,
                                       @PathVariable Long itemId, @RequestBody CommentDto inputCommentDto) {
        return itemService.saveComment(userId, itemId, inputCommentDto);
    }
}
