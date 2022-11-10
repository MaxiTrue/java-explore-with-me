package ru.practicum.explorewithme.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.compilation.service.CompilationServiceAdmin;
import ru.practicum.explorewithme.exception.ObjectNotFoundException;

@Slf4j
@Validated
@RequestMapping("/admin/compilations")
@RestController
@RequiredArgsConstructor
public class CompilationControllerAdmin {

    private final CompilationServiceAdmin compilationServiceAdmin;

    @PostMapping
    public ResponseEntity<Object> create(@Validated @RequestBody NewCompilationDto newCompilationDto)
            throws ObjectNotFoundException {
        log.debug("Request accepted POST: /admin/compilations");
        return ResponseEntity.status(HttpStatus.OK).body(compilationServiceAdmin.create(newCompilationDto));
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Object> delete(@PathVariable("compId") long compId) throws ObjectNotFoundException {
        compilationServiceAdmin.delete(compId);
        log.debug("Request accepted DELETE: /admin/compilations/{}", compId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public ResponseEntity<Object> addEventFromCompilation(@PathVariable("compId") long compId,
                                                          @PathVariable("eventId") long eventId)
            throws ObjectNotFoundException {
        log.debug("Request accepted PATCH: /admin/compilations/{}/events/{}", compId, eventId);
        compilationServiceAdmin.addEventFromCompilation(compId, eventId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public ResponseEntity<Object> deleteEventFromCompilation(@PathVariable("compId") long compId,
                                                             @PathVariable("eventId") long eventId)
            throws ObjectNotFoundException {
        log.debug("Request accepted DELETE: /admin/compilations/{}/events/{}", compId, eventId);
        compilationServiceAdmin.deleteEventFromCompilation(compId, eventId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{compId}/pin")
    public ResponseEntity<Object> pinCompilation(@PathVariable("compId") long compId) throws ObjectNotFoundException {
        log.debug("Request accepted PUTXH: /admin/compilations/{}/pin", compId);
        compilationServiceAdmin.setPinCompilation(compId, Boolean.TRUE);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{compId}/pin")
    public ResponseEntity<Object> unpinCompilation(@PathVariable("compId") long compId) throws ObjectNotFoundException {
        log.debug("Request accepted DELETE: /admin/compilations/{}/pin", compId);
        compilationServiceAdmin.setPinCompilation(compId, Boolean.FALSE);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
