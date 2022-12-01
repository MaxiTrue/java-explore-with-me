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

@Service
@RequiredArgsConstructor
public class CommentServiceAdminImpl implements CommentServiceAdmin {

    private final CommentStorage commentStorage;
    private final EventStorage eventStorage;
    private final CommentMapper commentMapper;

    @Override
    public CommentFullDto create(NewCommentDto commentDto, long eventId) throws ObjectNotFoundException {
        Event event = eventStorage.getByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new ObjectNotFoundException(eventId, "Event"));
        Comment comment = commentMapper.toCommentEntity(commentDto, null, event);

        return commentMapper.toCommentFullDto(commentStorage.save(comment));
    }

    @Override
    public CommentFullDto update(UpdateCommentDto commentDto, long eventId, long commId)
            throws ObjectNotFoundException, ValidException {
        eventStorage.getByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new ObjectNotFoundException(eventId, "Event"));
        Comment comment = commentStorage.findById(commId)
                .orElseThrow(() -> new ObjectNotFoundException(commId, "Comment"));

        // админ может обновить только комментарий от администратора
        // комментарий должен принадлежать событию
        if (!comment.getByAdmin() || eventId != comment.getEvent().getId()) {
            throw new ValidException("No rights to execute the operation.");
        }

        if (commentDto.getText() != null) comment.setText(commentDto.getText());
        comment.setChanged(Boolean.TRUE);

        return commentMapper.toCommentFullDto(commentStorage.save(comment));
    }

    @Override
    public CommentFullDto setPinComment(long eventId, long commId, boolean pin)
            throws ObjectNotFoundException, ValidException {
        eventStorage.getByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new ObjectNotFoundException(eventId, "Event"));
        Comment comment = commentStorage.findById(commId)
                .orElseThrow(() -> new ObjectNotFoundException(commId, "Comment"));

        // либо 1, либо 0
        long countPinnedComment = commentStorage.findCountPinnedCommentByEventIdAndPinned(eventId, Boolean.TRUE);

        // закрепить можно только один комментарий
        if (pin && countPinnedComment > 0) throw new ValidException("Only one comment can be pinned.");

        comment.setPinned(pin);
        return commentMapper.toCommentFullDto(commentStorage.save(comment));
    }

    @Override
    public void delete(long eventId, long commId) throws ObjectNotFoundException, ValidException {
        eventStorage.getByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new ObjectNotFoundException(eventId, "Event"));
        Comment comment = commentStorage.findById(commId)
                .orElseThrow(() -> new ObjectNotFoundException(commId, "Comment"));

        // комментарий должен принадлежать событию
        if (eventId != comment.getEvent().getId()) {
            throw new ValidException("No rights to execute the operation.");
        }

        commentStorage.delete(comment);
    }

}
