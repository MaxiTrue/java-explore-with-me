package ru.practicum.explorewithme.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.client.StatClient;
import ru.practicum.explorewithme.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.compilation.mapper.CompilationMapper;
import ru.practicum.explorewithme.compilation.model.Compilation;
import ru.practicum.explorewithme.compilation.storage.CompilationStorage;
import ru.practicum.explorewithme.event.dto.EventShortDto;
import ru.practicum.explorewithme.event.mapper.EventMapper;
import ru.practicum.explorewithme.exception.ObjectNotFoundException;
import ru.practicum.explorewithme.request.model.StatusRequest;
import ru.practicum.explorewithme.request.storage.ParticipationRequestStorage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServicePublicImpl implements CompilationServicePublic {

    private final CompilationStorage compilationStorage;
    private final ParticipationRequestStorage participationRequestStorage;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;
    private final StatClient statClient;


    @Override
    public List<CompilationDto> findAll(boolean pinned, PageRequest pageRequest) {
        List<Compilation> compilations = compilationStorage.findAllByPinned(pinned, pageRequest).getContent();

        return compilations.stream().map(compilation -> {
            Map<Long, Long> views = statClient.getViews(compilation.getEvents(), Boolean.FALSE);

            List<EventShortDto> eventShortDtoList = compilation.getEvents().stream().map(event -> {
                long confirmedRequests = participationRequestStorage
                        .findCountByEvenIdAndStatus(event.getId(), StatusRequest.CONFIRMED);
                return eventMapper.toEventShortDto(event, views.get(event.getId()), confirmedRequests);
            }).collect(Collectors.toList());

            return compilationMapper.toCompilationDto(compilation, eventShortDtoList);
        }).collect(Collectors.toList());
    }

    @Override
    public CompilationDto findById(long compId) throws ObjectNotFoundException {
        Compilation compilation = compilationStorage.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException(compId, "Compilation"));

        List<EventShortDto> eventShortDtoList = compilation.getEvents().stream().map(event -> {
            long views = statClient.getView(event, Boolean.FALSE);
            long confirmedRequests = participationRequestStorage
                    .findCountByEvenIdAndStatus(event.getId(), StatusRequest.CONFIRMED);
            return eventMapper.toEventShortDto(event, views, confirmedRequests);
        }).collect(Collectors.toList());

        return compilationMapper.toCompilationDto(compilation, eventShortDtoList);
    }
}
