package ru.practicum.explorewithme.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.comment.dto.CommentFullDto;
import ru.practicum.explorewithme.comment.dto.NewCommentDto;
import ru.practicum.explorewithme.comment.dto.UpdateCommentDto;
import ru.practicum.explorewithme.comment.mapper.CommentMapper;
import ru.practicum.explorewithme.comment.model.Comment;
import ru.practicum.explorewithme.comment.storage.CommentStorage;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventState;
import ru.practicum.explorewithme.event.storage.EventStorage;
import ru.practicum.explorewithme.exception.ObjectNotFoundException;
import ru.practicum.explorewithme.exception.ValidException;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.storage.UserStorage;

@Service
@RequiredArgsConstructor
public class CommentServicePublicImpl implements CommentServicePublic {

    private final CommentStorage commentStorage;
    private final UserStorage userStorage;
    private final EventStorage eventStorage;
    private final CommentMapper commentMapper;

    @Override
    public CommentFullDto create(NewCommentDto commentDto,
                                 long commentatorId,
                                 long eventId) throws ObjectNotFoundException {
        User commentator = userStorage.findById(commentatorId)
                .orElseThrow(() -> new ObjectNotFoundException(commentatorId, "User"));
        Event event = eventStorage.getByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new ObjectNotFoundException(eventId, "Event"));
        Comment comment = commentMapper.toCommentEntity(commentDto, commentator, event);
        return commentMapper.toCommentFullDto(commentStorage.save(comment));
    }

    @Override
    public CommentFullDto update(UpdateCommentDto commentDto, long commentatorId, long eventId, long commId)
            throws ObjectNotFoundException, ValidException {
        if (!userStorage.existsById(commentatorId)) throw new ObjectNotFoundException(commentatorId, "User");
        eventStorage.getByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new ObjectNotFoundException(eventId, "Event"));
        Comment comment = commentStorage.findById(commId)
                .orElseThrow(() -> new ObjectNotFoundException(commId, "Comment"));

        // комментатор должен быть автором комментария
        // комментарий должен принадлежать событию
        if ((commentatorId != comment.getCommentator().getId()) || (eventId != comment.getEvent().getId())) {
            throw new ValidException("No rights to execute the operation.");
        }

        if (commentDto.getText() != null) comment.setText(commentDto.getText());
        comment.setChanged(Boolean.TRUE);

        return commentMapper.toCommentFullDto(commentStorage.save(comment));
    }

    @Override
    public void delete(long commentatorId, long eventId, long commId) throws ObjectNotFoundException, ValidException {
        if (!userStorage.existsById(commentatorId)) throw new ObjectNotFoundException(commentatorId, "User");
        eventStorage.getByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new ObjectNotFoundException(eventId, "Event"));
        Comment comment = commentStorage.findById(commId)
                .orElseThrow(() -> new ObjectNotFoundException(commId, "Comment"));

        // комментатор должен быть автором комментария
        // комментарий должен принадлежать событию
        if ((commentatorId != comment.getCommentator().getId()) || eventId != comment.getEvent().getId()) {
            throw new ValidException("No rights to execute the operation.");
        }

        commentStorage.delete(comment);
    }

}
