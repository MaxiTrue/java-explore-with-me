package ru.practicum.explorewithme.comment.service;

import ru.practicum.explorewithme.comment.dto.CommentFullDto;
import ru.practicum.explorewithme.comment.dto.NewCommentDto;
import ru.practicum.explorewithme.comment.dto.UpdateCommentDto;
import ru.practicum.explorewithme.exception.ObjectNotFoundException;
import ru.practicum.explorewithme.exception.ValidException;

public interface CommentServiceAdmin {

    CommentFullDto create(NewCommentDto commentDto, long eventId) throws ObjectNotFoundException;

    CommentFullDto update(UpdateCommentDto commentDto, long eventId, long commId) throws ObjectNotFoundException, ValidException;

    CommentFullDto setPinComment(long eventId, long commId, boolean pin) throws ObjectNotFoundException, ValidException;

    void delete(long eventId, long commId) throws ObjectNotFoundException, ValidException;

}
