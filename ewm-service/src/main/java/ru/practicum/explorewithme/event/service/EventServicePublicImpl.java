package ru.practicum.explorewithme.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.client.StatClient;
import ru.practicum.explorewithme.comment.dto.CommentFullDto;
import ru.practicum.explorewithme.comment.mapper.CommentMapper;
import ru.practicum.explorewithme.comment.model.Comment;
import ru.practicum.explorewithme.comment.storage.CommentStorage;
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
import ru.practicum.explorewithme.request.servise.ParticipationRequestPrivateService;
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
    private final CommentStorage commentStorage;
    private final ParticipationRequestPrivateService participationRequestPrivateService;
    private final EventMapper eventMapper;
    private final CommentMapper commentMapper;
    private final StatClient statClient;

    @Override
    public EventFullDto findById(long eventId, HttpServletRequest request) throws ObjectNotFoundException {
        Event event = eventStorage.getByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new ObjectNotFoundException(eventId, "Event"));

        long views = statClient.getView(event, Boolean.FALSE);
        long confirmedRequests = participationRequestStorage
                .findCountByEvenIdAndStatus(eventId, StatusRequest.CONFIRMED);
        List<CommentFullDto> commentFullDtoList = commentStorage.findAllByEventId(event.getId())
                .stream().sorted(Comparator.comparing(Comment::getCommentDate))
                .map(commentMapper::toCommentFullDto).collect(Collectors.toList());

        EventFullDto eventFullDto = eventMapper.toEvenFullDto(event, views, confirmedRequests, commentFullDtoList);

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
        Map<Long, Long> mapRequest = participationRequestPrivateService.findAmountConfirmedRequestFromEvents(eventList);

        List<EventShortDto> eventShortDtoList = new ArrayList<>(eventList).stream()
                .map(event -> eventMapper.toEventShortDto(
                        event,
                        mapViews.get(event.getId()),
                        mapRequest.getOrDefault(event.getId(), 0L))).collect(Collectors.toList());

        if (eventParameters.getEventSort() == EventSort.VIEWS) {
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
