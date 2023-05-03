package ru.practicum.ewm.compilation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.event.dto.EventShortDto;

import java.time.LocalDateTime;
import java.util.Collection;

@Setter
@Getter
public class CompilationDto {
    private Long id;
    private Collection<EventInnerShortDto> events;
    private Boolean pinned;
    private String title;

    @Setter
    @Getter
    public static class EventInnerShortDto {

        private Long id;
        private String annotation;
        private EventShortDto.CategoryDto category;
        private Long confirmedRequests;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime eventDate;
        private EventShortDto.UserShortDto initiator;
        private Boolean paid;
        private String title;
        private Long views;
    }
}
