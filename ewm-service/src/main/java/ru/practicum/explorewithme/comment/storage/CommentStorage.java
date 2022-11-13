package ru.practicum.explorewithme.comment.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.comment.model.Comment;

import java.util.List;

public interface CommentStorage extends JpaRepository<Comment, Long> {

    List<Comment> findAllByEventId(long eventId);

    @Query(value = "SELECT COUNT (c.id) FROM Comment c WHERE c.event.id = :eventId AND c.pinned = :pin")
    Long findCountPinnedCommentByEventIdAndPinned(long eventId, boolean pin);
}
