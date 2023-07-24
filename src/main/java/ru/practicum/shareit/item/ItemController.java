package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ItemController {
    private final ItemMapper mapper;
    private final ItemService itemService;

    @Value("${defaultHeader}")
    private String defaultHeader;

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId) {
        return mapper.mapToDto(itemService.getItemById(itemId));
    }

    @GetMapping()
    public List<ItemDto> getAll(@RequestHeader("${defaultHeader}") Long userId) {
        return itemService.getAllItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemsByText(@RequestParam(value = "text", required = false) String text) {
        return itemService.searchItemsByText(text);
    }

    /**
     * Добавление новой вещи. Будет происходить по эндпоинту POST /items. На вход поступает объект ItemDto. userId в
     * заголовке X-Sharer-User-Id — это идентификатор пользователя, который добавляет вещь. Именно этот пользователь —
     * владелец вещи. Идентификатор владельца будет поступать на вход в каждом из запросов, рассмотренных далее.
     *
     * @return добавленная в БД вещь.
     */
    @PostMapping

    public ItemDto add(@RequestHeader(value = "${defaultHeader}", required = false) Long ownerId,
                       @RequestBody @Validated ItemDto itemDto) {
        return itemService.add(itemDto, ownerId);
    }

    @PatchMapping("{itemId}")
    public ItemDto update(@RequestHeader(value = "${defaultHeader}", required = false) Long ownerId,
                          @PathVariable Long itemId, @Validated @RequestBody ItemDto itemDto) {
        return itemService.updateInStorage(itemDto, ownerId, itemId);
    }
}