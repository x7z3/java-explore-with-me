package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.enums.EventSort;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

public interface EventService {
    Map<Long, Long> getViews(Collection<Event> events);

    EventFullDto addEvent(Long userId, NewEventDto eventDto);

    Collection<EventFullDto> findEvents(
            Long[] users,
            String[] states,
            Long[] categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Integer from,
            Integer size
    );

    Collection<EventFullDto> findEvents(Long userId, Integer from, Integer size);

    Collection<EventShortDto> findEvents(
            String text,
            Boolean paid,
            Boolean onlyAvailable,
            EventSort sort,
            Long[] categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Integer from,
            Integer size
    );

    EventFullDto patchEventByInitiator(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    EventFullDto patchEventByAdmin(Long eventId, UpdateEventAdminRequest updateRequest);

    EventRequestStatusUpdateResult changeRequestStatus(
            Long userId,
            Long eventId,
            EventRequestStatusUpdateRequest statusUpdateRequest
    );

    EventFullDto findEvent(Long id);

    Collection<ParticipationRequestDto> findEventRequests(Long userId, Long eventId);
}