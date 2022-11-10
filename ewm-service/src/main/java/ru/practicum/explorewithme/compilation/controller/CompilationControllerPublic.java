package ru.practicum.explorewithme.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.compilation.service.CompilationServicePublic;
import ru.practicum.explorewithme.exception.ObjectNotFoundException;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RequestMapping("/compilations")
@RestController
@RequiredArgsConstructor
public class CompilationControllerPublic {

    private final CompilationServicePublic compilationServicePublic;

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestParam(name = "pinned", defaultValue = "false") Boolean pinned,
                                          @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                          @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.debug("Request accepted GET: /compilations");
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return ResponseEntity.status(HttpStatus.OK).body(compilationServicePublic.findAll(pinned, pageRequest));
    }

    @GetMapping("/{compId}")
    public ResponseEntity<Object> findById(@PathVariable("compId") long compId) throws ObjectNotFoundException {
        log.debug("Request accepted GET: /compilations/{}", compId);
        return ResponseEntity.status(HttpStatus.OK).body(compilationServicePublic.findById(compId));
    }

}
