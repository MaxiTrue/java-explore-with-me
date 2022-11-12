package ru.practicum.explorewithme.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.dto.EndpointHitDto;
import ru.practicum.explorewithme.model.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class StatsMapper {

    public EndpointHit toHitEntity(EndpointHitDto endpointHit) {
        LocalDateTime timestamp = LocalDateTime
                .parse(endpointHit.getTimestamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return new EndpointHit(null, endpointHit.getApp(), endpointHit.getUri(), endpointHit.getIp(), timestamp);
    }

}
