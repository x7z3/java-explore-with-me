package ru.practicum.ewm.event.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.request.enums.ParticipationRequestStatusUpdate;

import java.util.Collection;

@Setter
@Getter
public class EventRequestStatusUpdateRequest {
    private Collection<Long> requestIds;
    private ParticipationRequestStatusUpdate status;
}
