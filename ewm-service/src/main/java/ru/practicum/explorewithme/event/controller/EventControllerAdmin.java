package ru.practicum.explorewithme.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.dto.AdminUpdateEventRequest;
import ru.practicum.explorewithme.event.model.EventParametersAdmin;
import ru.practicum.explorewithme.event.service.EventServiceAdmin;
import ru.practicum.explorewithme.exception.ObjectNotFoundException;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.xml.bind.ValidationException;
import java.util.List;


@Slf4j
@Validated
@RequestMapping("/admin/events")
@RestController
@RequiredArgsConstructor
public class EventControllerAdmin {

    private final EventServiceAdmin eventServiceAdmin;

    @GetMapping
    public ResponseEntity<Object> findEventsByFilterAdmin(
            @RequestParam(name = "users", required = false) List<Long> users,
            @RequestParam(name = "states", required = false) List<String> states,
            @RequestParam(name = "categories", required = false) List<Long> categories,
            @RequestParam(name = "rangeStart", required = false) String rangeStart,
            @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.debug("Request accepted GET: /admin/events");
        EventParametersAdmin parameters = new EventParametersAdmin(
                users, categories, states, rangeStart, rangeEnd, from, size);
        return ResponseEntity.status(HttpStatus.OK).body(eventServiceAdmin.findEventsByFilterAdmin(parameters));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<Object> updateEvent(@PathVariable("eventId") long eventId,
                                              @RequestBody AdminUpdateEventRequest updateRequest)
            throws ObjectNotFoundException {
        log.debug("Request accepted PUT: /admin/events/{}", eventId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventServiceAdmin.updateEvent(eventId, updateRequest));
    }

    @PatchMapping("/{eventId}/publish")
    public ResponseEntity<Object> publishEvent(@PathVariable("eventId") long eventId)
            throws ValidationException, ObjectNotFoundException {
        log.debug("Request accepted PATCH: /admin/events/{}/publish", eventId);
        return ResponseEntity.status(HttpStatus.OK).body(eventServiceAdmin.publishEvent(eventId));
    }

    @PatchMapping("/{eventId}/reject")
    public ResponseEntity<Object> rejectEvent(@PathVariable("eventId") long eventId)
            throws ValidationException, ObjectNotFoundException {
        log.debug("Request accepted PATCH: /admin/events/{}/reject", eventId);
        return ResponseEntity.status(HttpStatus.OK).body(eventServiceAdmin.rejectEvent(eventId));
    }
}
