package ru.practicum.shareit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShareItApp {
    public static void main(String[] args) {
        SpringApplication.run(ShareItApp.class, args);
        System.out.println("*".repeat(134) + "\n" + "*".repeat(40) + "       Спринт №14. " +
                "Add-Bookings. Сервер запущен.      "
                + "*".repeat(40) + "\n" + "*".repeat(134));
    }
}
