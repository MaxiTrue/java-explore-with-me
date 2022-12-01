package ru.practicum.explorewithme.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.client.StatClient;
import ru.practicum.explorewithme.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.compilation.mapper.CompilationMapper;
import ru.practicum.explorewithme.compilation.model.Compilation;
import ru.practicum.explorewithme.compilation.storage.CompilationStorage;
import ru.practicum.explorewithme.event.dto.EventShortDto;
import ru.practicum.explorewithme.event.mapper.EventMapper;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.storage.EventStorage;
import ru.practicum.explorewithme.exception.ObjectNotFoundException;
import ru.practicum.explorewithme.request.servise.ParticipationRequestPrivateService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceAdminImpl implements CompilationServiceAdmin {

    private final CompilationStorage compilationStorage;
    private final EventStorage eventStorage;
    private final ParticipationRequestPrivateService participationRequestPrivateService;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;
    private final StatClient statClient;

    @Override
    public CompilationDto create(NewCompilationDto newCompilationDto) throws ObjectNotFoundException {

        List<Event> events = new ArrayList<>();
        if (newCompilationDto.getEvents() != null) {
            for (Long id : newCompilationDto.getEvents()) {
                events.add(eventStorage.findById(id).orElseThrow(() -> new ObjectNotFoundException(id, "Event")));
            }
        }

        Compilation compilation = compilationStorage
                .save(compilationMapper.toCompilationEntity(newCompilationDto, events));

        Map<Long, Long> views = statClient.getViews(events, Boolean.FALSE);
        Map<Long, Long> mapRequest = participationRequestPrivateService.findAmountConfirmedRequestFromEvents(events);

        List<EventShortDto> eventShortDtoList = compilation.getEvents().stream()
                .map(event -> eventMapper.toEventShortDto(
                        event,
                        views.get(event.getId()),
                        mapRequest.getOrDefault(event.getId(), 0L))).collect(Collectors.toList());

        return compilationMapper.toCompilationDto(compilation, eventShortDtoList);

    }

    @Override
    public void delete(long compId) throws ObjectNotFoundException {
        Compilation compilation = compilationStorage.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException(compId, "Compilation"));
        compilationStorage.delete(compilation);
    }

    @Override
    public void addEventFromCompilation(long compId, long eventId) throws ObjectNotFoundException {
        Compilation compilation = compilationStorage.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException(compId, "Compilation"));
        Event event = eventStorage.findById(eventId).orElseThrow(() -> new ObjectNotFoundException(eventId, "Event"));

        List<Event> events = compilation.getEvents();
        events.add(event);
        compilation.setEvents(events);
        compilationStorage.save(compilation);
    }

    @Override
    public void deleteEventFromCompilation(long compId, long eventId) throws ObjectNotFoundException {
        Compilation compilation = compilationStorage.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException(compId, "Compilation"));
        Event event = eventStorage.findById(eventId).orElseThrow(() -> new ObjectNotFoundException(eventId, "Event"));

        List<Event> events = compilation.getEvents();
        events.remove(event);
        compilation.setEvents(events);
        compilationStorage.save(compilation);
    }

    @Override
    public void setPinCompilation(long compId, boolean pin) throws ObjectNotFoundException {
        Compilation compilation = compilationStorage.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException(compId, "Compilation"));
        compilation.setPinned(pin);
        compilationStorage.save(compilation);
    }


}
