package ru.practicum.explorewithme.compilation.service;

import ru.practicum.explorewithme.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.exception.ObjectNotFoundException;


public interface CompilationServiceAdmin {

    CompilationDto create(NewCompilationDto newCompilationDto) throws ObjectNotFoundException;

    void delete(long compId) throws ObjectNotFoundException;

    void addEventFromCompilation(long compId, long eventId) throws ObjectNotFoundException;

    void deleteEventFromCompilation(long compId, long eventId) throws ObjectNotFoundException;

    void setPinCompilation(long compId, boolean pin) throws ObjectNotFoundException;
}
