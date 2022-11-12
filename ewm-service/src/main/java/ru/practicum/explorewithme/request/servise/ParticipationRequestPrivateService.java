package ru.practicum.explorewithme.request.servise;

import ru.practicum.explorewithme.exception.ObjectNotFoundException;
import ru.practicum.explorewithme.exception.ValidException;
import ru.practicum.explorewithme.request.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestPrivateService {

    ParticipationRequestDto create(long userId, long eventId) throws ObjectNotFoundException, ValidException;

    ParticipationRequestDto cancel(long userId, long requestId) throws ObjectNotFoundException, ValidException;

    List<ParticipationRequestDto> findAllByRequesterId(long requestId) throws ObjectNotFoundException;
}
