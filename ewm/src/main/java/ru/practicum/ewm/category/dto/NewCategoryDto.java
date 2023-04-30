package ru.practicum.ewm.category.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class NewCategoryDto {
    @NotBlank
    private String name;
}
