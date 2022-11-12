package ru.practicum.explorewithme.comment.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.comment.model.Comment;

import java.util.List;

public interface CommentStorage extends JpaRepository<Comment, Long> {

    List<Comment> findAllByEventId(long eventId);
}
