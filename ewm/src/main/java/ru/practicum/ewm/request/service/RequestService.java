package ru.practicum.ewm.request.service;

import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.Collection;

public interface RequestService {
    ParticipationRequestDto addRequest(Long userId, Long eventId);

    Collection<ParticipationRequestDto> findRequests(Long userId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}
