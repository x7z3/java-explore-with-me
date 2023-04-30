package ru.practicum.ewm.compilation.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class NewCompilationDto {
    private Long[] events;
    private Boolean pinned;
    @NotBlank
    private String title;
}
