package ru.practicum.explorewithme.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.Create;
import ru.practicum.explorewithme.Update;
import ru.practicum.explorewithme.event.dto.NewEventDto;
import ru.practicum.explorewithme.event.dto.UpdateEventRequest;
import ru.practicum.explorewithme.event.service.EventServicePrivate;
import ru.practicum.explorewithme.exception.ObjectNotFoundException;
import ru.practicum.explorewithme.exception.ValidException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class EventControllerPrivate {

    private final EventServicePrivate eventServicePrivate;

    @GetMapping("/{userId}/events")
    public ResponseEntity<Object> findAllByUserId(
            @PathVariable("userId") long userId,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
            @PositiveOrZero @RequestParam(name = "size", defaultValue = "10") int size,
            HttpServletRequest request) throws ObjectNotFoundException {
        log.debug("Request accepted GET: /users/{}/events", userId);
        Pageable pageable = PageRequest.of(from / size, size);
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventServicePrivate.findAllByUserId(userId, pageable, request));
    }

    @PostMapping("{userId}/events")
    public ResponseEntity<Object> create(@Validated({Create.class}) @RequestBody NewEventDto newEventDto,
                                         @PathVariable("userId") long userId) throws ObjectNotFoundException {
        log.debug("Request accepted POST: /users/{}/events", userId);
        return ResponseEntity.status(HttpStatus.OK).body(eventServicePrivate.create(newEventDto, userId));
    }

    @PatchMapping("{userId}/events")
    public ResponseEntity<Object> update(@Validated({Update.class}) @RequestBody UpdateEventRequest updateEventRequest,
                                         @PathVariable("userId") long userId)
            throws ObjectNotFoundException, ValidException {
        log.debug("Request accepted PATCH: /users/{}/events", userId);
        return ResponseEntity.status(HttpStatus.OK).body(eventServicePrivate.update(updateEventRequest, userId));
    }

    @GetMapping("{userId}/events/{eventId}")
    public ResponseEntity<Object> findByUserIdAndEventId(@PathVariable("userId") long userId,
                                                         @PathVariable("eventId") long eventId,
                                                         HttpServletRequest request)
            throws ObjectNotFoundException, ValidException {
        log.debug("Request accepted GET: /users/{}/events/{}", userId, eventId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventServicePrivate.findByUserIdAndEventId(userId, eventId, request));
    }

    @PatchMapping("{userId}/events/{eventId}")
    public ResponseEntity<Object> cancelEvent(@PathVariable("userId") long userId,
                                              @PathVariable("eventId") long eventId)
            throws ObjectNotFoundException, ValidException {
        log.debug("Request accepted PATCH: /users/{}/events/{}", userId, eventId);
        return ResponseEntity.status(HttpStatus.OK).body(eventServicePrivate.cancelEvent(userId, eventId));
    }

    @GetMapping("{userId}/events/{eventId}/requests")
    public ResponseEntity<Object> findAllRequestByEventIdAndUserId(@PathVariable("userId") long userId,
                                                                   @PathVariable("eventId") long eventId)
            throws ObjectNotFoundException, ValidException {
        log.debug("Request accepted GET: /users/{}/events/{}/requests", userId, eventId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventServicePrivate.findAllRequestByEventIdAndUserId(userId, eventId));
    }

    @PatchMapping("{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ResponseEntity<Object> confirmRequest(@PathVariable("userId") long userId,
                                                 @PathVariable("eventId") long eventId,
                                                 @PathVariable("reqId") long reqId)
            throws ObjectNotFoundException, ValidException {
        log.debug("Request accepted PATCH: /users/{}/events/{}/requests/{}/confirm", userId, eventId, reqId);
        return ResponseEntity.status(HttpStatus.OK).body(eventServicePrivate.confirmRequest(userId, eventId, reqId));
    }

    @PatchMapping("{userId}/events/{eventId}/requests/{reqId}/reject")
    public ResponseEntity<Object> rejectRequest(@PathVariable("userId") long userId,
                                                @PathVariable("eventId") long eventId,
                                                @PathVariable("reqId") long reqId)
            throws ObjectNotFoundException, ValidException {
        log.debug("Request accepted PATCH: /users/{}/events/{}/requests/{}/reject", userId, eventId, reqId);
        return ResponseEntity.status(HttpStatus.OK).body(eventServicePrivate.rejectRequest(userId, eventId, reqId));
    }

}
