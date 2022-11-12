package ru.practicum.explorewithme.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.comment.dto.NewCommentDto;
import ru.practicum.explorewithme.comment.dto.UpdateCommentDto;
import ru.practicum.explorewithme.comment.service.CommentServiceAdmin;
import ru.practicum.explorewithme.exception.ObjectNotFoundException;
import ru.practicum.explorewithme.exception.ValidException;

@Slf4j
@Validated
@RequestMapping("/admin/events/{eventId}/comments")
@RestController
@RequiredArgsConstructor
public class CommentControllerAdmin {

    private final CommentServiceAdmin commentServiceAdmin;

    @PostMapping
    ResponseEntity<Object> create(@Validated @RequestBody NewCommentDto commentDto,
                                  @PathVariable("eventId") long eventId) throws ObjectNotFoundException {
        commentDto.setByAdmin(Boolean.TRUE);
        commentDto.setPinned(Boolean.FALSE);
        commentDto.setChanged(Boolean.FALSE);
        return ResponseEntity.status(HttpStatus.OK).body(commentServiceAdmin.create(commentDto, eventId));
    }

    @PatchMapping("/{commId}")
    ResponseEntity<Object> update(@Validated @RequestBody UpdateCommentDto commentDto,
                                  @PathVariable("eventId") long eventId,
                                  @PathVariable("commId") long commId) throws ObjectNotFoundException, ValidException {
        return ResponseEntity.status(HttpStatus.OK).body(commentServiceAdmin.update(commentDto, eventId, commId));
    }

    @PatchMapping("/{commId}/pin")
    ResponseEntity<Object> pinComment(@PathVariable("eventId") long eventId,
                                      @PathVariable("commId") long commId)
            throws ObjectNotFoundException, ValidException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentServiceAdmin.setPinComment(eventId, commId, Boolean.TRUE));
    }

    @DeleteMapping("/{commId}/pin")
    ResponseEntity<Object> unpinComment(@PathVariable("eventId") long eventId,
                                        @PathVariable("commId") long commId)
            throws ObjectNotFoundException, ValidException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentServiceAdmin.setPinComment(eventId, commId, Boolean.FALSE));
    }

    @DeleteMapping("/{commId}")
    ResponseEntity<Object> delete(@PathVariable("eventId") long eventId,
                                  @PathVariable("commId") long commId)
            throws ObjectNotFoundException, ValidException {
        commentServiceAdmin.delete(eventId, commId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
