package ru.practicum.statistics.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.statistics.dto.EndpointHitDto;
import ru.practicum.statistics.dto.ViewStatsDto;
import ru.practicum.statistics.server.StatsRepository;
import ru.practicum.statistics.server.mapper.EndpointHitMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    public void saveHit(EndpointHitDto hitDto) {
        statsRepository.save(EndpointHitMapper.toEndpointHit(hitDto));
    }

    @Override
    public List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        if (unique) {
            if (uris != null && uris.length > 0) {
                return statsRepository.getUniqueViewStatsByUris(start, end, uris);
            } else {
                return statsRepository.getUniqueViewStats(start, end);
            }
        } else {
            if (uris != null && uris.length > 0) {
                return statsRepository.getViewStatsByUris(start, end, uris);
            } else {
                return statsRepository.getViewStats(start, end);
            }
        }
    }
}
