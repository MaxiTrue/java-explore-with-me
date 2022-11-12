package ru.practicum.explorewithme.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.client.StatClient;
import ru.practicum.explorewithme.event.dto.EndpointHit;
import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.dto.EventShortDto;
import ru.practicum.explorewithme.event.mapper.EventMapper;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventParameters;
import ru.practicum.explorewithme.event.model.EventSort;
import ru.practicum.explorewithme.event.model.EventState;
import ru.practicum.explorewithme.event.storage.EventStorage;
import ru.practicum.explorewithme.exception.ObjectNotFoundException;
import ru.practicum.explorewithme.request.model.StatusRequest;
import ru.practicum.explorewithme.request.storage.ParticipationRequestStorage;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServicePublicImpl implements EventServicePublic {

    @Value("${app.name}")
    private String appName;

    private final EventStorage eventStorage;
    private final ParticipationRequestStorage participationRequestStorage;
    private final EventMapper eventMapper;
    private final StatClient statClient;

    @Override
    public EventFullDto findById(long eventId, HttpServletRequest request) throws ObjectNotFoundException {
        Event event = eventStorage.getByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new ObjectNotFoundException(eventId, "Event"));

        long views = statClient.getView(event, Boolean.FALSE);
        long confirmedRequests = participationRequestStorage
                .findCountByEvenIdAndStatus(eventId, StatusRequest.CONFIRMED);

        EventFullDto eventFullDto = eventMapper.toEvenFullDto(event, views, confirmedRequests);

        statClient.saveHit(EndpointHit.builder()
                .app(appName)
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build());

        return eventFullDto;
    }

    @Override
    public List<EventShortDto> findEventsByFilter(EventParameters eventParameters, HttpServletRequest request) {
        List<Event> eventList = eventStorage.findEventsByFilter(
                eventParameters.getText(),
                eventParameters.getCategories(),
                eventParameters.getPaid(),
                eventParameters.getRangeStart(),
                eventParameters.getRangeEnd(),
                eventParameters.getPageRequest()).getContent();

        if (eventList.size() == 0) return new ArrayList<>();

        if (eventParameters.getEventSort() == EventSort.EVENT_DATE && eventList.size() > 1) {
            eventList.sort((o1, o2) -> o2.getEventDate().compareTo(o1.getEventDate()));
        }

        Map<Long, Long> mapViews = statClient.getViews(eventList, Boolean.FALSE);

        List<EventShortDto> eventShortDtoList = new ArrayList<>();
        for (Event event : eventList) {
            long confirmedRequests = participationRequestStorage
                    .findCountByEvenIdAndStatus(event.getId(), StatusRequest.CONFIRMED);
            eventShortDtoList.add(eventMapper.toEventShortDto(event, mapViews.get(event.getId()), confirmedRequests));
        }

        if (eventParameters.getEventSort() == EventSort.VIEWS && eventList.size() > 1) {
            eventShortDtoList.sort((o1, o2) -> o2.getViews().compareTo(o1.getViews()));
        }

        List<EndpointHit> endpointHitList = eventList.stream().map(event -> EndpointHit.builder()
                .app(appName)
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI() + "/" + event.getId())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build()).collect(Collectors.toList());
        statClient.saveHits(endpointHitList);

        return eventShortDtoList;
    }

}
