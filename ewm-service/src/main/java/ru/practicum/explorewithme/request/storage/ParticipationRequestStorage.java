package ru.practicum.explorewithme.request.storage;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.request.model.ParticipationRequest;
import ru.practicum.explorewithme.request.model.StatusRequest;

import java.util.List;
import java.util.Optional;

public interface ParticipationRequestStorage extends JpaRepository<ParticipationRequest, Long> {

    // запрос по id события и id автора запроса
    Optional<ParticipationRequest> findByEventIdAndRequesterId(long eventId, long requesterId);

    // все запросы по id события и статусу
    List<ParticipationRequest> findAllByEventIdAndStatus(long eventId, StatusRequest statusRequest);

    // все запросы одного пользователя
    List<ParticipationRequest> findAllByRequesterId(long requesterId);

    // все запросы для одного события
    List<ParticipationRequest> findAllByEventId(long eventId);

}
