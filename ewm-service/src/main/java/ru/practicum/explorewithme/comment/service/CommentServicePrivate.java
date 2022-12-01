package ru.practicum.explorewithme.comment.service;

import ru.practicum.explorewithme.comment.dto.CommentFullDto;
import ru.practicum.explorewithme.comment.dto.NewCommentDto;
import ru.practicum.explorewithme.comment.dto.UpdateCommentDto;
import ru.practicum.explorewithme.exception.ObjectNotFoundException;
import ru.practicum.explorewithme.exception.ValidException;

public interface CommentServicePrivate {

    CommentFullDto create(NewCommentDto commentDto, long commentatorId, long eventId) throws ObjectNotFoundException;

    CommentFullDto update(UpdateCommentDto commentDto, long commentatorId, long eventId, long commId) throws ObjectNotFoundException, ValidException;

    void delete(long commentatorId, long eventId, long commId) throws ObjectNotFoundException, ValidException;
}
