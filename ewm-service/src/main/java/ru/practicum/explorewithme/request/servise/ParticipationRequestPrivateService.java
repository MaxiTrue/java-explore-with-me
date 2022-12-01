package ru.practicum.explorewithme.request.servise;

import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.exception.ObjectNotFoundException;
import ru.practicum.explorewithme.exception.ValidException;
import ru.practicum.explorewithme.request.dto.ParticipationRequestDto;

import java.util.List;
import java.util.Map;

public interface ParticipationRequestPrivateService {

    ParticipationRequestDto create(long userId, long eventId) throws ObjectNotFoundException, ValidException;

    ParticipationRequestDto cancel(long userId, long requestId) throws ObjectNotFoundException, ValidException;

    List<ParticipationRequestDto> findAllByRequesterId(long requestId) throws ObjectNotFoundException;

    Map<Long, Long> findAmountConfirmedRequestFromEvents(List<Event> events);
}
