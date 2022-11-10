package ru.practicum.explorewithme.request.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.request.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.request.model.ParticipationRequest;
import ru.practicum.explorewithme.request.model.StatusRequest;
import ru.practicum.explorewithme.user.model.User;

import java.time.format.DateTimeFormatter;

@Component
public class ParticipationRequestMapper {

    public ParticipationRequest toParticipationRequestEntity(Event event, User requester) {
        ParticipationRequest participationRequest = new ParticipationRequest();
        participationRequest.setEvent(event);
        participationRequest.setRequester(requester);
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            participationRequest.setStatus(StatusRequest.CONFIRMED);
        }
        return participationRequest;
    }

    public ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        return ParticipationRequestDto.builder()
                .id(participationRequest.getId())
                .created(participationRequest.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .event(participationRequest.getEvent().getId())
                .requester(participationRequest.getRequester().getId())
                .status(participationRequest.getStatus().name()).build();
    }

}
