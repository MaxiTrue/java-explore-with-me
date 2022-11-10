package ru.practicum.explorewithme.event.service;

import ru.practicum.explorewithme.event.dto.AdminUpdateEventRequest;
import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.model.EventParametersAdmin;
import ru.practicum.explorewithme.exception.ObjectNotFoundException;

import javax.xml.bind.ValidationException;
import java.util.List;

public interface EventServiceAdmin {

    List<EventFullDto> findEventsByFilterAdmin(EventParametersAdmin parameters);

    EventFullDto updateEvent(long eventId, AdminUpdateEventRequest updateRequest) throws ObjectNotFoundException;

    EventFullDto publishEvent(long eventId) throws ObjectNotFoundException, ValidationException;

    EventFullDto rejectEvent(long eventId) throws ObjectNotFoundException, ValidationException;

}
