package ru.practicum.explorewithme.compilation.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.explorewithme.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.exception.ObjectNotFoundException;

import java.util.List;

public interface CompilationServicePublic {

    List<CompilationDto> findAll(boolean pinned, PageRequest pageRequest);

    CompilationDto findById(long compId) throws ObjectNotFoundException;
}
