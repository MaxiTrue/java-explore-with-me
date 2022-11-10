package ru.practicum.explorewithme.compilation.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.compilation.model.Compilation;
import ru.practicum.explorewithme.event.dto.EventShortDto;
import ru.practicum.explorewithme.event.model.Event;

import java.util.List;

@Component
public class CompilationMapper {

    public Compilation toCompilationEntity(NewCompilationDto newCompilationDto, List<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setEvents(events);
        compilation.setPinned(newCompilationDto.getPinned());
        compilation.setTitle(newCompilationDto.getTitle());
        return compilation;
    }

    public CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> eventShortDtoList) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(eventShortDtoList)
                .pinned(compilation.getPinned())
                .title(compilation.getTitle()).build();
    }
}
