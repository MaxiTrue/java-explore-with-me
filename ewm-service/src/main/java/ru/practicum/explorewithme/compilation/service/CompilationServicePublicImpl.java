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
import ru.practicum.explorewithme.request.servise.ParticipationRequestPrivateService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServicePublicImpl implements CompilationServicePublic {

    private final CompilationStorage compilationStorage;
    private final ParticipationRequestPrivateService participationRequestPrivateService;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;
    private final StatClient statClient;


    @Override
    public List<CompilationDto> findAll(boolean pinned, PageRequest pageRequest) {
        List<Compilation> compilations = compilationStorage.findAllByPinned(pinned, pageRequest).getContent();

        return compilations.stream().map(compilation -> {
            Map<Long, Long> mapViews = statClient.getViews(compilation.getEvents(), Boolean.FALSE);
            Map<Long, Long> mapRequest = participationRequestPrivateService
                    .findAmountConfirmedRequestFromEvents(compilation.getEvents());

            List<EventShortDto> eventShortDtoList = compilation.getEvents().stream()
                    .map(event -> eventMapper.toEventShortDto(
                            event,
                            mapViews.get(event.getId()),
                            mapRequest.getOrDefault(event.getId(), 0L))).collect(Collectors.toList());

            return compilationMapper.toCompilationDto(compilation, eventShortDtoList);
        }).collect(Collectors.toList());
    }

    @Override
    public CompilationDto findById(long compId) throws ObjectNotFoundException {
        Compilation compilation = compilationStorage.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException(compId, "Compilation"));

        Map<Long, Long> mapViews = statClient.getViews(compilation.getEvents(), Boolean.FALSE);
        Map<Long, Long> mapRequest = participationRequestPrivateService
                .findAmountConfirmedRequestFromEvents(compilation.getEvents());

        List<EventShortDto> eventShortDtoList = compilation.getEvents().stream()
                .map(event -> eventMapper.toEventShortDto(
                        event,
                        mapViews.get(event.getId()),
                        mapRequest.getOrDefault(event.getId(), 0L))).collect(Collectors.toList());

        return compilationMapper.toCompilationDto(compilation, eventShortDtoList);
    }
}
