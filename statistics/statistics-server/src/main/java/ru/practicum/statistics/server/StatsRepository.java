package ru.practicum.statistics.server;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.statistics.dto.ViewStatsDto;
import ru.practicum.statistics.server.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("SELECT NEW ru.practicum.statistics.dto.ViewStatsDto(h.app, h.uri, COUNT(h.id)) " +
            "FROM EndpointHit h " +
            "WHERE h.timestamp > :start " +
            "  AND h.timestamp < :end " +
            "GROUP BY h.app, h.uri ORDER BY COUNT(h.id) DESC")
    List<ViewStatsDto> getViewStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT NEW ru.practicum.statistics.dto.ViewStatsDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM EndpointHit h " +
            "WHERE h.timestamp > :start " +
            "  AND h.timestamp < :end " +
            "GROUP BY h.app, h.uri ORDER BY COUNT(h.id) DESC")
    List<ViewStatsDto> getUniqueViewStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT NEW ru.practicum.statistics.dto.ViewStatsDto(h.app, h.uri, COUNT(h.id)) " +
            "FROM EndpointHit h " +
            "WHERE h.timestamp > :start " +
            "  AND h.timestamp < :end " +
            "  AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri ORDER BY COUNT(h.id) DESC")
    List<ViewStatsDto> getViewStatsByUris(@Param("start") LocalDateTime start,
                                          @Param("end") LocalDateTime end,
                                          @Param("uris") String[] uris);

    @Query("SELECT NEW ru.practicum.statistics.dto.ViewStatsDto(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM EndpointHit h " +
            "WHERE h.timestamp > :start " +
            "  AND h.timestamp < :end " +
            "  AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri ORDER BY COUNT(h.id) DESC")
    List<ViewStatsDto> getUniqueViewStatsByUris(@Param("start") LocalDateTime start,
                                                @Param("end") LocalDateTime end,
                                                @Param("uris") String[] uris);
}
