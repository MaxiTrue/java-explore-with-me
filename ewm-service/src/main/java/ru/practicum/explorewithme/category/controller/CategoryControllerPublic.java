package ru.practicum.explorewithme.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.category.service.CategoryServicePublic;
import ru.practicum.explorewithme.exception.ObjectNotFoundException;

import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RequestMapping("/categories")
@RestController
@RequiredArgsConstructor
public class CategoryControllerPublic {

    private final CategoryServicePublic categoryService;

    @GetMapping
    public ResponseEntity<Object> findAll(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                          @PositiveOrZero @RequestParam(name = "size", defaultValue = "10") int size) {
        log.debug("Request accepted GET: /admin/categories");
        Pageable pageable = PageRequest.of(from / size, size);
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.findAll(pageable));
    }

    @GetMapping("/{catId}")
    public ResponseEntity<Object> findById(@PathVariable("catId") long catId) throws ObjectNotFoundException {
        log.debug("Request accepted GET: /admin/categories/{}", catId);
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.findById(catId));
    }
}
