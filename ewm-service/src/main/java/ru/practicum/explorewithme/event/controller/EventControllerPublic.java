package ru.practicum.explorewithme.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.model.EventParameters;
import ru.practicum.explorewithme.event.service.EventServicePublic;
import ru.practicum.explorewithme.exception.ObjectNotFoundException;
import ru.practicum.explorewithme.exception.ValidException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RequestMapping("/events")
@RestController
@RequiredArgsConstructor
public class EventControllerPublic {

    private final EventServicePublic eventServicePublic;

    @GetMapping
    public ResponseEntity<Object> findEventsByFilter(
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "categories", required = false) List<Long> categories,
            @RequestParam(name = "paid", required = false) Boolean paid,
            @RequestParam(name = "rangeStart", required = false) String rangeStart,
            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(name = "onlyAvailable", required = false) Boolean onlyAvailable,
            @RequestParam(name = "sort", required = false, defaultValue = "VIEWS") String sort,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
            HttpServletRequest request) throws ValidException {
        log.debug("Request accepted GET: /events");
        EventParameters parameters = new EventParameters(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return ResponseEntity.status(HttpStatus.OK).body(eventServicePublic.findEventsByFilter(parameters, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") long id, HttpServletRequest request)
            throws ObjectNotFoundException {
        log.debug("Request accepted GET: /events{}", id);
        return ResponseEntity.status(HttpStatus.OK).body(eventServicePublic.findById(id, request));
    }

}
