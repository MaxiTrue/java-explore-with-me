package ru.practicum.explorewithme.event.service;

import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.dto.EventShortDto;
import ru.practicum.explorewithme.event.model.EventParameters;
import ru.practicum.explorewithme.exception.ObjectNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventServicePublic {

    EventFullDto findById(long eventId, HttpServletRequest request) throws ObjectNotFoundException;

    List<EventShortDto> findEventsByFilter(EventParameters eventParameters, HttpServletRequest request);
}
