package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.EndpointHitDto;
import ru.practicum.explorewithme.dto.ViewStats;
import ru.practicum.explorewithme.servise.StatsService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    public ResponseEntity<Object> saveHit(@Validated @RequestBody EndpointHitDto endpointHitDto) {
        log.debug("Request accepted POST: /hit");
        statsService.saveHit(endpointHitDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/hits")
    public ResponseEntity<Object> saveHits(@Validated @RequestBody List<EndpointHitDto> endpointHitDtoList) {
        log.debug("Request accepted POST: /hit");
        statsService.saveHits(endpointHitDtoList);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(
            @RequestParam("start") String start,
            @RequestParam("end") String end,
            @RequestParam(value = "uris", required = false) List<String> uris,
            @RequestParam(value = "unique", defaultValue = "false", required = false) Boolean unique) {
        log.debug("Request accepted GET: /stats");
        return statsService.getViews(uris, unique, start, end);
    }

}
