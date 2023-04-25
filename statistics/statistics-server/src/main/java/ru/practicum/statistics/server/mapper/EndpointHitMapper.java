package ru.practicum.statistics.server.mapper;

import ru.practicum.statistics.dto.EndpointHitDto;
import ru.practicum.statistics.server.model.EndpointHit;

public final class EndpointHitMapper {
    private EndpointHitMapper() {
    }

    public static EndpointHit toEndpointHit(EndpointHitDto hitDto) {
        return new EndpointHit(hitDto.getId(),
                hitDto.getApp(),
                hitDto.getUri(),
                hitDto.getIp(),
                hitDto.getTimestamp());
    }
}
