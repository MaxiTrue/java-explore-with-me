package ru.practicum.explorewithme.servise;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.EndpointHitDto;
import ru.practicum.explorewithme.dto.ViewStats;
import ru.practicum.explorewithme.mapper.StatsMapper;
import ru.practicum.explorewithme.srorage.StatsStorage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsStorage hitStorage;
    private final StatsMapper statsMapper;

    @Override
    public void save(List<EndpointHitDto> endpointHitDtoList) {
        hitStorage.saveAll(endpointHitDtoList.stream().map(statsMapper::toHitEntity).collect(Collectors.toList()));
    }

    public List<ViewStats> getViews(List<String> uris, Boolean unique, String start, String end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (unique) return hitStorage.getViewStatsUnique(uris,
                LocalDateTime.parse(start, formatter), LocalDateTime.parse(end, formatter));

        return hitStorage.getViewStats(uris,
                LocalDateTime.parse(start, formatter), LocalDateTime.parse(end, formatter));
    }

}
