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
    public ResponseEntity<Object> save(@Validated @RequestBody EndpointHitDto endpointHit) {
        log.debug("Request accepted POST: /hit");
        statsService.save(endpointHit);
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
