package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.event.Location;
import ru.practicum.ewm.event.enums.StateUserAction;

import java.time.LocalDateTime;

@Setter
@Getter
public class UpdateEventUserRequest {
    private String annotation;
    private CategoryDto category;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateUserAction stateAction;
    private String title;

    @Setter
    @Getter
    public static class CategoryDto {
        private Long id;
        private String name;
    }
}