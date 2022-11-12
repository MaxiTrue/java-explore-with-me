package ru.practicum.explorewithme.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.exception.ObjectNotFoundException;
import ru.practicum.explorewithme.exception.ValidException;
import ru.practicum.explorewithme.request.servise.ParticipationRequestPrivateService;

@Slf4j
@Validated
@RequestMapping("/users/{userId}/requests")
@RestController
@RequiredArgsConstructor
public class ParticipationRequestControllerPrivate {

    private final ParticipationRequestPrivateService participationRequestService;

    @PostMapping()
    public ResponseEntity<Object> create(@PathVariable("userId") long userId, @RequestParam("eventId") long eventId)
            throws ObjectNotFoundException, ValidException {
        log.debug("Request accepted POST: /users/{}/requests", userId);
        return ResponseEntity.status(HttpStatus.OK).body(participationRequestService.create(userId, eventId));
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<Object> cancel(@PathVariable("userId") long userId, @PathVariable("requestId") long requestId)
            throws ObjectNotFoundException, ValidException {
        log.debug("Request accepted PATCH: /users/{}/requests/{}/cancel", userId, requestId);
        return ResponseEntity.status(HttpStatus.OK).body(participationRequestService.cancel(userId, requestId));
    }

    @GetMapping()
    public ResponseEntity<Object> findAllByRequesterId(@PathVariable("userId") long userId)
            throws ObjectNotFoundException {
        log.debug("Request accepted GET: /users/{}/requests", userId);
        return ResponseEntity.status(HttpStatus.OK).body(participationRequestService.findAllByRequesterId(userId));
    }
}
