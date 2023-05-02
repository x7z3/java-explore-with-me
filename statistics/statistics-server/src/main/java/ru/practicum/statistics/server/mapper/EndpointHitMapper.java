package ru.practicum.statistics.server.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.statistics.dto.EndpointHitDto;
import ru.practicum.statistics.server.model.EndpointHit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EndpointHitMapper {
    public static EndpointHit toEndpointHit(EndpointHitDto hitDto) {
        return new EndpointHit(hitDto.getId(),
                hitDto.getApp(),
                hitDto.getUri(),
                hitDto.getIp(),
                hitDto.getTimestamp());
    }
}
