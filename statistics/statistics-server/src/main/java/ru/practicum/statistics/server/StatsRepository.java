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

    @Query("select new ru.practicum.statistics.dto.ViewStatsDto(h.app, h.uri, count(h.id)) " +
            "from EndpointHit h " +
            "where h.timestamp > :start " +
            "  and h.timestamp < :end " +
            "group by h.app, h.uri order by count(h.id) DESC")
    List<ViewStatsDto> getViewStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("select new ru.practicum.statistics.dto.ViewStatsDto(h.app, h.uri, count(distinct h.ip)) " +
            "from EndpointHit h " +
            "where h.timestamp > :start " +
            "  and h.timestamp < :end " +
            "group by h.app, h.uri order by count(h.id) DESC")
    List<ViewStatsDto> getUniqueViewStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("select new ru.practicum.statistics.dto.ViewStatsDto(h.app, h.uri, count(h.id)) " +
            "from EndpointHit h " +
            "where h.timestamp > :start " +
            "  and h.timestamp < :end " +
            "  and h.uri in :uris " +
            "group by h.app, h.uri order by count(h.id) DESC")
    List<ViewStatsDto> getViewStatsByUris(@Param("start") LocalDateTime start,
                                          @Param("end") LocalDateTime end,
                                          @Param("uris") String[] uris);

    @Query("select new ru.practicum.statistics.dto.ViewStatsDto(h.app, h.uri, count(distinct h.ip)) " +
            "from EndpointHit h " +
            "where h.timestamp > :start " +
            "  and h.timestamp < :end " +
            "  and h.uri in :uris " +
            "group by h.app, h.uri order by count(h.id) DESC")
    List<ViewStatsDto> getUniqueViewStatsByUris(@Param("start") LocalDateTime start,
                                                @Param("end") LocalDateTime end,
                                                @Param("uris") String[] uris);
}
