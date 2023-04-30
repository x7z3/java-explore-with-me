package ru.practicum.ewm.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class NewUserRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String email;
}
