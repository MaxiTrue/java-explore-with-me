package ru.practicum.explorewithme.comment.mapper;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.comment.dto.CommentFullDto;
import ru.practicum.explorewithme.comment.dto.NewCommentDto;
import ru.practicum.explorewithme.comment.model.Comment;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.user.model.User;

import java.time.format.DateTimeFormatter;

@Component
public class CommentMapper {

    public Comment toCommentEntity(NewCommentDto commentDto, @Nullable User commentator, Event event) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setEvent(event);
        comment.setCommentator(commentator);
        comment.setByAdmin(commentDto.getByAdmin());
        comment.setPinned(commentDto.getPinned());
        comment.setChanged(commentDto.getChanged());

        return comment;
    }

    public CommentFullDto toCommentFullDto(Comment comment) {
        return CommentFullDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .eventId(comment.getEvent().getId())
                .commentatorId(comment.getCommentator() == null ? null : comment.getCommentator().getId())
                .commentDate(comment.getCommentDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .byAdmin(comment.getByAdmin())
                .pinned(comment.getPinned())
                .changed(comment.getChanged()).build();
    }
}
