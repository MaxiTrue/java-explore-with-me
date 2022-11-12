package ru.practicum.explorewithme.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.storage.CategoryStorage;
import ru.practicum.explorewithme.client.StatClient;
import ru.practicum.explorewithme.comment.dto.CommentFullDto;
import ru.practicum.explorewithme.comment.mapper.CommentMapper;
import ru.practicum.explorewithme.comment.storage.CommentStorage;
import ru.practicum.explorewithme.event.dto.AdminUpdateEventRequest;
import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.mapper.EventMapper;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventParametersAdmin;
import ru.practicum.explorewithme.event.model.EventState;
import ru.practicum.explorewithme.event.storage.EventStorage;
import ru.practicum.explorewithme.exception.ObjectNotFoundException;
import ru.practicum.explorewithme.location.model.Location;
import ru.practicum.explorewithme.location.storage.LocationStorage;
import ru.practicum.explorewithme.request.model.StatusRequest;
import ru.practicum.explorewithme.request.storage.ParticipationRequestStorage;

import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceAdminImpl implements EventServiceAdmin {

    private final EventStorage eventStorage;
    private final ParticipationRequestStorage participationRequestStorage;
    private final CategoryStorage categoryStorage;
    private final LocationStorage locationStorage;
    private final CommentStorage commentStorage;
    private final CommentMapper commentMapper;
    private final EventMapper eventMapper;
    private final StatClient statClient;


    @Override
    public List<EventFullDto> findEventsByFilterAdmin(EventParametersAdmin parameters) {
        List<Event> events = eventStorage.findEventsByFilterAdmin(
                parameters.getUsers(),
                parameters.getCategories(),
                parameters.getRangeStart(),
                parameters.getRangeEnd(),
                parameters.getStates(),
                parameters.getPageRequest()).getContent();

        Map<Long, Long> mapViews = statClient.getViews(events, Boolean.FALSE);

        return events.stream().map(event -> {
            long confirmedRequests = participationRequestStorage
                    .findCountByEvenIdAndStatus(event.getId(), StatusRequest.CONFIRMED);
            List<CommentFullDto> commentFullDtoList = commentStorage.findAllByEventId(event.getId())
                    .stream().map(commentMapper::toCommentFullDto).collect(Collectors.toList());
            return eventMapper.toEvenFullDto(event, mapViews.get(event.getId()), confirmedRequests, commentFullDtoList);
        }).collect(Collectors.toList());

    }

    @Override
    public EventFullDto updateEvent(long eventId, AdminUpdateEventRequest updateRequest)
            throws ObjectNotFoundException {
        Event event = eventStorage.findById(eventId).orElseThrow((() -> new ObjectNotFoundException(eventId, "Event")));

        if (updateRequest.getAnnotation() != null) {
            event.setAnnotation(updateRequest.getAnnotation());
        }
        if (updateRequest.getCategory() != null) {
            Category category = categoryStorage.findById(updateRequest.getCategory())
                    .orElseThrow((() -> new ObjectNotFoundException(updateRequest.getCategory(), "Category")));
            event.setCategory(category);
        }
        if (updateRequest.getDescription() != null) {
            event.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(
                    updateRequest.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if (updateRequest.getLocation() != null) {
            Location location = locationStorage
                    .findLocationByLatAndLon(updateRequest.getLocation().getLat(), updateRequest.getLocation().getLon())
                    .orElse(new Location(null, updateRequest.getLocation().getLat(), updateRequest.getLocation().getLon()));
            event.setLocation(location);
        }
        if (updateRequest.getPaid() != null) {
            event.setPaid(updateRequest.getPaid());
        }
        if (updateRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateRequest.getParticipantLimit());
        }
        if (updateRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateRequest.getRequestModeration());
        }
        if (updateRequest.getTitle() != null) {
            event.setTitle(updateRequest.getTitle());
        }

        eventStorage.save(event);

        long views = statClient.getView(event, Boolean.FALSE);
        long confirmedRequest = participationRequestStorage
                .findCountByEvenIdAndStatus(eventId, StatusRequest.CONFIRMED);
        List<CommentFullDto> commentFullDtoList = commentStorage.findAllByEventId(event.getId())
                .stream().map(commentMapper::toCommentFullDto).collect(Collectors.toList());
        return eventMapper.toEvenFullDto(event, views, confirmedRequest, commentFullDtoList);
    }

    @Override
    public EventFullDto publishEvent(long eventId) throws ObjectNotFoundException, ValidationException {
        Event event = eventStorage.findById(eventId).orElseThrow((() -> new ObjectNotFoundException(eventId, "Event")));
        LocalDateTime publishedDate = LocalDateTime.now();
        if (event.getState() != EventState.PENDING || publishedDate.isAfter(event.getEventDate().plusHours(1))) {
            throw new ValidationException("For the requested operation the conditions are not met.");
        }

        event.setPublishedOn(publishedDate);
        event.setState(EventState.PUBLISHED);
        eventStorage.save(event);

        return eventMapper.toEvenFullDto(event, 0L, 0L, new ArrayList<>());
    }

    @Override
    public EventFullDto rejectEvent(long eventId) throws ObjectNotFoundException, ValidationException {
        Event event = eventStorage.findById(eventId).orElseThrow((() -> new ObjectNotFoundException(eventId, "Event")));
        if (event.getState() == EventState.PUBLISHED) {
            throw new ValidationException("For the requested operation the conditions are not met.");
        }

        event.setState(EventState.CANCELED);
        eventStorage.save(event);

        return eventMapper.toEvenFullDto(event, 0L, 0L, new ArrayList<>());
    }
}
