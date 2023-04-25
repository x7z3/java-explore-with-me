package ru.practicum.statistics.server.service;

import ru.practicum.statistics.dto.EndpointHitDto;
import ru.practicum.statistics.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void saveHit(EndpointHitDto hitDto);

    List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique);
}
