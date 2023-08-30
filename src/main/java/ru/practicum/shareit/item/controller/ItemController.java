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
    public ItemWithBookingAndCommentsDto getItemByIdForOwner(@RequestHeader(value = "X-Sharer-User-Id") Long ownerId,
                                                             @PathVariable Long itemId) {
        log.debug("получен запрос @RequestMapping(\"/items\") @GetMapping(\"/{itemId}\")" + ownerId + itemId);
        ItemWithBookingAndCommentsDto dto = itemService.getItemWithBookingAndComment(itemId, ownerId);
        log.debug("Отправлен ответ: " + dto.toString());
        //return itemService.getItemWithBookingAndComment(itemId, ownerId);
        return dto;
    }

    @GetMapping()
    public List<ItemWithBookingAndCommentsDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {

        log.info("получен запрос @RequestMapping(\"/items\") @GetMapping()" + userId);
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
    public ItemDto add(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long ownerId,
                       @RequestBody @Validated ItemDto itemDto) {

        log.trace("A TRACE Message");
        log.debug("A DEBUG Message");
        log.info("An INFO Message");
        log.warn("A WARN Message");
        log.error("An ERROR Message");

        log.info("получен запрос @RequestMapping(\"/items\") @PostMapping" + ownerId + itemDto.toString());
        ItemDto dto = itemService.add(itemDto, ownerId);
        log.info("Отправлен ответ" + dto.toString());

        return dto;
    }

    @PatchMapping("{itemId}")
    public ItemDto update(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long ownerId,
                          @PathVariable Long itemId, @Validated @RequestBody ItemDto itemDto) {
        System.out.println(" - Обновление вещи с ID = " + itemId + " юзера с ID = " + ownerId + ".");
        return itemService.updateInStorage(itemId, itemDto, ownerId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addCommentToItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @PathVariable Long itemId, @RequestBody CommentDto inputCommentDto) {
        return itemService.saveComment(userId, itemId, inputCommentDto);
    }


}
