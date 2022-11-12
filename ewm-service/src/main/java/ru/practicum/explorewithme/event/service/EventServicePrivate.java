package ru.practicum.explorewithme.event.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.dto.EventShortDto;
import ru.practicum.explorewithme.event.dto.NewEventDto;
import ru.practicum.explorewithme.event.dto.UpdateEventRequest;
import ru.practicum.explorewithme.exception.ObjectNotFoundException;
import ru.practicum.explorewithme.exception.ValidException;
import ru.practicum.explorewithme.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventServicePrivate {

    EventFullDto create(NewEventDto newEventDto, long userId) throws ObjectNotFoundException;

    EventFullDto update(UpdateEventRequest updateEventRequest, long userId) throws ObjectNotFoundException, ValidException;

    List<EventShortDto> findAllByUserId(long userId, Pageable pageable, HttpServletRequest request) throws ObjectNotFoundException;

    EventFullDto findByUserIdAndEventId(long userId, long eventId, HttpServletRequest request) throws ObjectNotFoundException, ValidException;

    EventFullDto cancelEvent(long userId, long eventId) throws ObjectNotFoundException, ValidException;

    List<ParticipationRequestDto> findAllRequestByEventIdAndUserId(long userId, long eventId) throws ObjectNotFoundException, ValidException;

    ParticipationRequestDto confirmRequest(long userId, long eventId, long reqId) throws ObjectNotFoundException, ValidException;

    ParticipationRequestDto rejectRequest(long userId, long eventId, long reqId) throws ObjectNotFoundException, ValidException;
}
