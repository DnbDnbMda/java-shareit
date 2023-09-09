package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

//мдау@Getter
//мдау@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentDto {

    @NotBlank
    private String text;
}
