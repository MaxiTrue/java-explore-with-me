package ru.practicum.explorewithme.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.comment.dto.NewCommentDto;
import ru.practicum.explorewithme.comment.dto.UpdateCommentDto;
import ru.practicum.explorewithme.comment.service.CommentServicePublic;
import ru.practicum.explorewithme.exception.ObjectNotFoundException;
import ru.practicum.explorewithme.exception.ValidException;

@Slf4j
@Validated
@RequestMapping("/users/{userId}/events/{eventId}/comments")
@RestController
@RequiredArgsConstructor
public class CommentControllerPublic {

    private final CommentServicePublic commentServicePublic;

    @PostMapping
    ResponseEntity<Object> create(@Validated @RequestBody NewCommentDto commentDto,
                                  @PathVariable("userId") long commentatorId,
                                  @PathVariable("eventId") long eventId) throws ObjectNotFoundException {
        commentDto.setByAdmin(Boolean.FALSE);
        commentDto.setPinned(Boolean.FALSE);
        commentDto.setChanged(Boolean.FALSE);
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentServicePublic.create(commentDto, commentatorId, eventId));
    }

    @PatchMapping("/{commId}")
    ResponseEntity<Object> update(@Validated @RequestBody UpdateCommentDto commentDto,
                                  @PathVariable("userId") long commentatorId,
                                  @PathVariable("eventId") long eventId,
                                  @PathVariable("commId") long commId) throws ObjectNotFoundException, ValidException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentServicePublic.update(commentDto, commentatorId, eventId, commId));
    }

    @DeleteMapping("{commId}")
    ResponseEntity<Object> delete(@PathVariable("userId") long commentatorId,
                                  @PathVariable("eventId") long eventId,
                                  @PathVariable("commId") long commId) throws ObjectNotFoundException, ValidException {
        commentServicePublic.delete(commentatorId, eventId, commId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
