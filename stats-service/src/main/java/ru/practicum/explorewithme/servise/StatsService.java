package ru.practicum.explorewithme.servise;

import ru.practicum.explorewithme.dto.EndpointHitDto;
import ru.practicum.explorewithme.dto.ViewStats;

import java.util.List;

public interface StatsService {

    void save(EndpointHitDto endpointHit);

    List<ViewStats> getViews(List<String> uris, Boolean unique, String start, String end);

}
