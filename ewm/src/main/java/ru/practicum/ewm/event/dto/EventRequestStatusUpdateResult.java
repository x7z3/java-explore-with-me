package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.request.enums.ParticipationRequestStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Setter
@Getter
public class EventRequestStatusUpdateResult {
    private Collection<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
    private Collection<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

    @Setter
    @Getter
    public static class ParticipationRequestDto {
        private Long id;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime created;
        private Long event;
        private Long requester;
        private ParticipationRequestStatus status;
    }
}
