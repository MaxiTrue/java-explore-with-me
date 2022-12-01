package ru.practicum.explorewithme.srorage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explorewithme.dto.ViewStats;
import ru.practicum.explorewithme.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsStorage extends JpaRepository<EndpointHit, Long> {

    @Query(value = "SELECT NEW ru.practicum.explorewithme.dto.ViewStats(e.app, e.uri, COUNT(e.ip)) " +
            "FROM EndpointHit AS e " +
            "WHERE e.timestamp BETWEEN :start AND :end " +
            "AND (:uris IS NULL OR e.uri IN :uris) " +
            "GROUP BY e.uri, e.app")
    List<ViewStats> getViewStats(@Param("uris") List<String> uris, @Param("start") LocalDateTime start,
                                 @Param("end") LocalDateTime end);

    @Query(value = "SELECT NEW ru.practicum.explorewithme.dto.ViewStats(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
            "FROM EndpointHit AS e " +
            "WHERE e.timestamp BETWEEN :start AND :end " +
            "AND (:uris IS NULL OR e.uri IN :uris) " +
            "GROUP BY e.uri, e.app")
    List<ViewStats> getViewStatsUnique(@Param("uris") List<String> uris, @Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end);


}
