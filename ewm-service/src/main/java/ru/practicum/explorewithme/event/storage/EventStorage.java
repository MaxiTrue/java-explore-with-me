package ru.practicum.explorewithme.event.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventStorage extends JpaRepository<Event, Long> {

    Optional<Event> getByIdAndState(long eventId, EventState stateEvent);

    @Query(value = "SELECT e FROM Event e " +
            "WHERE ((:text IS NULL OR LOWER(e.annotation) LIKE CONCAT('%', LOWER(:text) , '%')) " +
            "OR (:text IS NULL OR LOWER(e.description) LIKE CONCAT('%', LOWER(:text), '%'))) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND e.eventDate BETWEEN :rangeStart AND :rangeEnd AND e.state = 'PUBLISHED'")
    Page<Event> findEventsByFilter(@Param("text") String text, @Param("categories") List<Long> categories,
                                   @Param("paid") Boolean paid, @Param("rangeStart") LocalDateTime rangeStart,
                                   @Param("rangeEnd") LocalDateTime rangeEnd, Pageable pageable);


    @Query(value = "SELECT e FROM Event e " +
            "WHERE ((:users IS NULL OR e.organizer.id IN :users)) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (:states IS NULL OR e.state IN :states) " +
            "AND e.eventDate BETWEEN :rangeStart AND :rangeEnd")
    Page<Event> findEventsByFilterAdmin(@Param("users") List<Long> users, @Param("categories") List<Long> categories,
                                        @Param("rangeStart") LocalDateTime rangeStart,
                                        @Param("rangeEnd") LocalDateTime rangeEnd,
                                        @Param("states") List<EventState> states, Pageable pageable);


    Page<Event> findAllByOrganizerId(long organizerId, Pageable pageable);
}
