package ru.practicum.explorewithme.request.storage;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.request.dto.ConfirmedRequestsDto;
import ru.practicum.explorewithme.request.model.ParticipationRequest;
import ru.practicum.explorewithme.request.model.StatusRequest;

import java.util.List;
import java.util.Optional;

public interface ParticipationRequestStorage extends JpaRepository<ParticipationRequest, Long> {

    // запрос по id события и id автора запроса
    Optional<ParticipationRequest> findByEventIdAndRequesterId(long eventId, long requesterId);

    // все запросы по id события и статусу
    List<ParticipationRequest> findAllByEventIdAndStatus(long eventId, StatusRequest statusRequest);

    // количество запросов, по id события и статусу
    @Query(value = "SELECT COUNT (pr.id) FROM ParticipationRequest pr " +
            "WHERE pr.event.id = :eventId AND pr.status = :statusRequest")
    Long findCountByEvenIdAndStatus(long eventId, StatusRequest statusRequest);

    // количество запросов, по списку id событий
    @Query(value = "SELECT NEW ru.practicum.explorewithme.request.dto.ConfirmedRequestsDto(pr.event.id, COUNT (pr.id))" +
            " FROM ParticipationRequest pr " +
            "WHERE (pr.event.id IN :eventsId AND pr.status = :statusRequest)" +
            "GROUP BY pr.event.id")
    List<ConfirmedRequestsDto> findCountByEvenIdsAndStatus(List<Long> eventsId, StatusRequest statusRequest);

    // все запросы одного пользователя
    List<ParticipationRequest> findAllByRequesterId(long requesterId);

    // все запросы для одного события
    List<ParticipationRequest> findAllByEventId(long eventId);

}
