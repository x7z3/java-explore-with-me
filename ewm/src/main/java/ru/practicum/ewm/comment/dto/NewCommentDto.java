package ru.practicum.ewm.comment.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class NewCommentDto {
    @NotBlank
    private String text;
}
