package ru.practicum.ewm.request;

import ru.practicum.ewm.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.Collection;
import java.util.stream.Collectors;

public final class RequestMapper {
    private RequestMapper() {
    }

    public static ParticipationRequestDto toRequestDto(ParticipationRequest request) {
        ParticipationRequestDto requestDto = new ParticipationRequestDto();
        requestDto.setId(request.getId());
        requestDto.setCreated(request.getCreated());
        requestDto.setEvent(request.getEvent().getId());
        requestDto.setRequester(request.getRequesterId());
        requestDto.setStatus(request.getStatus());
        return requestDto;
    }

    public static EventRequestStatusUpdateResult.ParticipationRequestDto toRequestInnerDto(ParticipationRequest request) {
        EventRequestStatusUpdateResult.ParticipationRequestDto requestDto =
                new EventRequestStatusUpdateResult.ParticipationRequestDto();
        requestDto.setId(request.getId());
        requestDto.setCreated(request.getCreated());
        requestDto.setEvent(request.getEvent().getId());
        requestDto.setRequester(request.getRequesterId());
        requestDto.setStatus(request.getStatus());
        return requestDto;
    }

    public static Collection<ParticipationRequestDto> toRequestDto(Collection<ParticipationRequest> requests) {
        return requests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toUnmodifiableList());
    }
}
