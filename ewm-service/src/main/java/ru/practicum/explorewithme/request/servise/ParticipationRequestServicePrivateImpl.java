package ru.practicum.explorewithme.request.servise;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventState;
import ru.practicum.explorewithme.event.storage.EventStorage;
import ru.practicum.explorewithme.exception.ObjectNotFoundException;
import ru.practicum.explorewithme.exception.ValidException;
import ru.practicum.explorewithme.request.dto.ConfirmedRequestsDto;
import ru.practicum.explorewithme.request.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.request.mapper.ParticipationRequestMapper;
import ru.practicum.explorewithme.request.model.ParticipationRequest;
import ru.practicum.explorewithme.request.model.StatusRequest;
import ru.practicum.explorewithme.request.storage.ParticipationRequestStorage;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipationRequestServicePrivateImpl implements ParticipationRequestPrivateService {

    private final ParticipationRequestStorage participationRequestStorage;
    private final UserStorage userStorage;
    private final EventStorage eventStorage;
    private final ParticipationRequestMapper participationRequestMapper;

    @Override
    public ParticipationRequestDto create(long userId, long eventId) throws ObjectNotFoundException, ValidException {

        User user = userStorage.findById(userId).orElseThrow(() -> new ObjectNotFoundException(userId, "User"));
        Event event = eventStorage.getByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new ObjectNotFoundException(eventId, "Event"));

        Optional<ParticipationRequest> existingRequest = participationRequestStorage
                .findByEventIdAndRequesterId(userId, eventId);

        if (existingRequest.isPresent()) throw new ValidException("Request already exists.");
        if (event.getState() != EventState.PUBLISHED) throw new ValidException("Request not published.");
        if (event.getOrganizer().getId() == userId) throw new ValidException("Request for your event.");

        //если лимит = 0 тогда воспринимаем как безлимит
        if (event.getParticipantLimit() != 0L) {
            long amountConfirmedRequest = participationRequestStorage
                    .findCountByEvenIdAndStatus(eventId, StatusRequest.CONFIRMED);
            if (amountConfirmedRequest >= event.getParticipantLimit()) {
                throw new ValidException("All places are occupied.");
            }
        }

        ParticipationRequest participationRequest = participationRequestMapper
                .toParticipationRequestEntity(event, user);

        return participationRequestMapper.toParticipationRequestDto(participationRequestStorage
                .save(participationRequest));
    }

    @Override
    public ParticipationRequestDto cancel(long userId, long requestId) throws ObjectNotFoundException, ValidException {

        if (!userStorage.existsById(userId)) throw new ObjectNotFoundException(userId, "User");
        ParticipationRequest participationRequest = participationRequestStorage.findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException(requestId, "Request"));

        if (participationRequest.getRequester().getId() != userId)
            throw new ValidException("Only pending request can be changed.");
        if (participationRequest.getStatus() != StatusRequest.PENDING)
            throw new ValidException("Сan only change your request.");

        participationRequest.setStatus(StatusRequest.CANCELED);

        return participationRequestMapper
                .toParticipationRequestDto(participationRequestStorage.save(participationRequest));
    }

    @Override
    public List<ParticipationRequestDto> findAllByRequesterId(long requestId) throws ObjectNotFoundException {
        if (!userStorage.existsById(requestId)) throw new ObjectNotFoundException(requestId, "User");
        List<ParticipationRequest> requests = participationRequestStorage.findAllByRequesterId(requestId);

        return requests.stream()
                .map(participationRequestMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    @Override
    public Map<Long, Long> findAmountConfirmedRequestFromEvents(List<Event> events) {

        List<Long> ids = new ArrayList<>(events).stream().map(Event::getId).collect(Collectors.toList());

        List<ConfirmedRequestsDto> confirmedRequestsDtoList = participationRequestStorage
                .findCountByEvenIdsAndStatus(ids, StatusRequest.CONFIRMED);

        Map<Long, Long> amountRequestFromEvents = new HashMap<>();
        for (ConfirmedRequestsDto requestsDto : confirmedRequestsDtoList) {
            amountRequestFromEvents.put(requestsDto.getEventId(), requestsDto.getAmountRequest());
        }

        return amountRequestFromEvents;
    }

}
