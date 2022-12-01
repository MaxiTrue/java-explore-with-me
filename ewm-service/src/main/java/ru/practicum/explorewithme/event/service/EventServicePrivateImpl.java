package ru.practicum.explorewithme.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.storage.CategoryStorage;
import ru.practicum.explorewithme.client.StatClient;
import ru.practicum.explorewithme.comment.dto.CommentFullDto;
import ru.practicum.explorewithme.comment.mapper.CommentMapper;
import ru.practicum.explorewithme.comment.model.Comment;
import ru.practicum.explorewithme.comment.storage.CommentStorage;
import ru.practicum.explorewithme.event.dto.EventFullDto;
import ru.practicum.explorewithme.event.dto.EventShortDto;
import ru.practicum.explorewithme.event.dto.NewEventDto;
import ru.practicum.explorewithme.event.dto.UpdateEventRequest;
import ru.practicum.explorewithme.event.mapper.EventMapper;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventState;
import ru.practicum.explorewithme.exception.ValidException;
import ru.practicum.explorewithme.location.model.Location;
import ru.practicum.explorewithme.event.storage.EventStorage;
import ru.practicum.explorewithme.location.storage.LocationStorage;
import ru.practicum.explorewithme.exception.ObjectNotFoundException;
import ru.practicum.explorewithme.request.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.request.mapper.ParticipationRequestMapper;
import ru.practicum.explorewithme.request.model.ParticipationRequest;
import ru.practicum.explorewithme.request.model.StatusRequest;
import ru.practicum.explorewithme.request.servise.ParticipationRequestPrivateService;
import ru.practicum.explorewithme.request.storage.ParticipationRequestStorage;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.storage.UserStorage;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServicePrivateImpl implements EventServicePrivate {

    private final EventStorage eventStorage;
    private final UserStorage userStorage;
    private final CategoryStorage categoryStorage;
    private final LocationStorage locationStorage;
    private final ParticipationRequestStorage participationRequestStorage;
    private final CommentStorage commentStorage;
    private final ParticipationRequestPrivateService participationRequestPrivateService;
    private final CommentMapper commentMapper;
    private final EventMapper eventMapper;
    private final ParticipationRequestMapper participationRequestMapper;
    private final StatClient statClient;

    @Override
    public EventFullDto create(NewEventDto newEventDto, long userId) throws ObjectNotFoundException {
        User user = userStorage.findById(userId).orElseThrow(() -> new ObjectNotFoundException(userId, "User"));
        Category category = categoryStorage.findById(newEventDto.getCategory())
                .orElseThrow(() -> new ObjectNotFoundException(newEventDto.getCategory(), "Category"));
        Event event = eventMapper.toEventEntity(newEventDto);
        event.setOrganizer(user);
        event.setCategory(category);
        Location location = locationStorage
                .findLocationByLatAndLon(newEventDto.getLocation().getLat(), newEventDto.getLocation().getLon())
                .orElseGet(() -> locationStorage
                        .save(new Location(
                                null,
                                newEventDto.getLocation().getLat(),
                                newEventDto.getLocation().getLon())));
        event.setLocation(location);
        return eventMapper.toEvenFullDto(eventStorage.save(event), 0L, 0L, new ArrayList<>());
    }

    @Override
    public EventFullDto update(UpdateEventRequest updateEventRequest, long userId)
            throws ObjectNotFoundException, ValidException {
        if (!userStorage.existsById(userId)) throw new ObjectNotFoundException(userId, "User");
        Event event = eventStorage.findById(updateEventRequest.getEventId())
                .orElseThrow((() -> new ObjectNotFoundException(updateEventRequest.getEventId(), "Event")));

        if (userId != event.getOrganizer().getId()) {
            throw new ValidException("The update is only available for native events.");
        }

        if (event.getState() == EventState.PUBLISHED) {
            throw new ValidException("You can only update events with statuses PENDING or REJECTED.");
        }

        if (updateEventRequest.getCategory() != null) {
            Category category = categoryStorage.findById(updateEventRequest.getCategory())
                    .orElseThrow((() -> new ObjectNotFoundException(updateEventRequest.getCategory(), "Category")));
            event.setCategory(category);
        }

        if (updateEventRequest.getEventDate() != null) {
            LocalDateTime eventDate = LocalDateTime.parse(
                    updateEventRequest.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new ValidException("Invalid time for event.");
            }
            event.setEventDate(eventDate);
        }

        if (updateEventRequest.getAnnotation() != null) event.setAnnotation(updateEventRequest.getAnnotation());
        if (updateEventRequest.getDescription() != null) event.setDescription(updateEventRequest.getDescription());
        if (updateEventRequest.getPaid() != null) event.setPaid(updateEventRequest.getPaid());
        if (updateEventRequest.getTitle() != null) event.setTitle(updateEventRequest.getTitle());

        if (updateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }

        if (event.getState() == EventState.CANCELED) event.setState(EventState.PENDING);

        return eventMapper.toEvenFullDto(eventStorage.save(event), 0L, 0L, new ArrayList<>());
    }

    @Override
    public List<EventShortDto> findAllByUserId(long userId, Pageable pageable, HttpServletRequest request)
            throws ObjectNotFoundException {
        if (!userStorage.existsById(userId)) throw new ObjectNotFoundException(userId, "User");

        List<Event> events = eventStorage.findAllByOrganizerId(userId, pageable).getContent();

        Map<Long, Long> mapViews = statClient.getViews(events, Boolean.FALSE);
        Map<Long, Long> mapRequests = participationRequestPrivateService.findAmountConfirmedRequestFromEvents(events);

        return events.stream().map(event -> eventMapper.toEventShortDto(
                event,
                mapViews.get(event.getId()),
                mapRequests.getOrDefault(event.getId(), 0L))).collect(Collectors.toList());
    }

    @Override
    public EventFullDto findByUserIdAndEventId(long userId, long eventId, HttpServletRequest request)
            throws ObjectNotFoundException, ValidException {
        if (!userStorage.existsById(userId)) throw new ObjectNotFoundException(userId, "User");
        Event event = eventStorage.findById(eventId).orElseThrow((() -> new ObjectNotFoundException(eventId, "Event")));
        if (event.getOrganizer().getId() != userId) throw new ValidException("No rights to execute the operation.");

        long views = 0;
        long confirmedRequests = 0;
        List<CommentFullDto> commentFullDtoList = new ArrayList<>();
        if (event.getState() == EventState.PUBLISHED) {
            views = statClient.getView(event, Boolean.FALSE);
            confirmedRequests = participationRequestStorage
                    .findCountByEvenIdAndStatus(eventId, StatusRequest.CONFIRMED);
            commentFullDtoList = commentStorage.findAllByEventId(event.getId())
                    .stream().sorted(Comparator.comparing(Comment::getCommentDate))
                    .map(commentMapper::toCommentFullDto).collect(Collectors.toList());
        }

        return eventMapper.toEvenFullDto(event, views, confirmedRequests, commentFullDtoList);
    }

    @Override
    public EventFullDto cancelEvent(long userId, long eventId) throws ObjectNotFoundException, ValidException {
        if (!userStorage.existsById(userId)) throw new ObjectNotFoundException(userId, "User");
        Event event = eventStorage.findById(eventId).orElseThrow((() -> new ObjectNotFoundException(eventId, "Event")));
        if (event.getOrganizer().getId() != userId) throw new ValidException("No rights to execute the operation.");
        if (event.getState() == EventState.PUBLISHED) throw new ValidException("You can't cancel a published event.");
        event.setState(EventState.CANCELED);

        return eventMapper.toEvenFullDto(eventStorage.save(event), 0L, 0L, new ArrayList<>());
    }

    @Override
    public List<ParticipationRequestDto> findAllRequestByEventIdAndUserId(long userId, long eventId)
            throws ObjectNotFoundException, ValidException {
        if (!userStorage.existsById(userId)) throw new ObjectNotFoundException(userId, "User");
        Event event = eventStorage.findById(eventId).orElseThrow((() -> new ObjectNotFoundException(eventId, "Event")));
        if (event.getOrganizer().getId() != userId) throw new ValidException("No rights to execute the operation.");

        List<ParticipationRequest> requests = participationRequestStorage.findAllByEventId(eventId);

        return requests.stream().map(participationRequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto confirmRequest(long userId, long eventId, long reqId)
            throws ObjectNotFoundException, ValidException {
        if (!userStorage.existsById(userId)) throw new ObjectNotFoundException(userId, "User");
        Event event = eventStorage.findById(eventId).orElseThrow((() -> new ObjectNotFoundException(eventId, "Event")));
        if (event.getOrganizer().getId() != userId) throw new ValidException("No rights to execute the operation.");
        ParticipationRequest request = participationRequestStorage.findById(reqId)
                .orElseThrow((() -> new ObjectNotFoundException(reqId, "Request")));

        // если запрос не относится к нашему событию, считаем что запрос не нашли
        if (request.getEvent().getId() != eventId) throw new ObjectNotFoundException(reqId, "Request");

        // может быть сразу подтверждён если у события отключена модерация или лимит заявок = 0
        if (request.getStatus() == StatusRequest.CONFIRMED) {
            return participationRequestMapper.toParticipationRequestDto(request);
        }

        // проверим не исчерпан ли лимит на заявки
        long confirmedRequests = participationRequestStorage
                .findCountByEvenIdAndStatus(event.getId(), StatusRequest.CONFIRMED);
        if (confirmedRequests == event.getParticipantLimit()) throw new ValidException("Limit has been exhausted.");

        request.setStatus(StatusRequest.CONFIRMED);

        // сначала сохраним, ниже получаем все не подтвержденные
        ParticipationRequest confirmedRequest = participationRequestStorage.save(request); // его и вернём

        // отклоним все оставшиеся заявки если текущей заявкой исчерпали лимит
        if (confirmedRequests + 1L == event.getParticipantLimit()) {
            List<ParticipationRequest> participationRequests = participationRequestStorage
                    .findAllByEventIdAndStatus(eventId, StatusRequest.PENDING);
            for (ParticipationRequest pendingRequest : participationRequests) {
                pendingRequest.setStatus(StatusRequest.REJECTED);
            }
            participationRequestStorage.saveAll(participationRequests);
        }

        return participationRequestMapper.toParticipationRequestDto(confirmedRequest);
    }

    @Override
    public ParticipationRequestDto rejectRequest(long userId, long eventId, long reqId)
            throws ObjectNotFoundException, ValidException {
        if (!userStorage.existsById(userId)) throw new ObjectNotFoundException(userId, "User");
        Event event = eventStorage.findById(eventId).orElseThrow((() -> new ObjectNotFoundException(eventId, "Event")));
        if (event.getOrganizer().getId() != userId) throw new ValidException("No rights to execute the operation.");
        ParticipationRequest request = participationRequestStorage.findById(reqId)
                .orElseThrow((() -> new ObjectNotFoundException(reqId, "Request")));

        // если запрос не относится к нашему событию, считаем что запрос не нашли
        if (request.getEvent().getId() != eventId) throw new ObjectNotFoundException(reqId, "Request");

        request.setStatus(StatusRequest.REJECTED);
        ParticipationRequest canceledRequest = participationRequestStorage.save(request);

        return participationRequestMapper.toParticipationRequestDto(canceledRequest);
    }

}
