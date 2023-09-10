package ru.practicum.shareit.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    //В итоге решил оставить исходный вариант с константами, потому что с application.properties постоянно
    //какие-то ошибки.
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";
    public static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DEFAULT_FROM_VALUE = "0";
    public static final String DEFAULT_SIZE_VALUE = "20";
}
