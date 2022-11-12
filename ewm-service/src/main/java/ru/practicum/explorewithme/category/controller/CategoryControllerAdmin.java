package ru.practicum.explorewithme.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.constaints.Create;
import ru.practicum.explorewithme.constaints.Update;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.dto.NewCategoryDto;
import ru.practicum.explorewithme.category.service.CategoryServiceAdmin;
import ru.practicum.explorewithme.exception.ObjectNotFoundException;

@Slf4j
@Validated
@RequestMapping("/admin/categories")
@RestController
@RequiredArgsConstructor
public class CategoryControllerAdmin {

    private final CategoryServiceAdmin categoryService;

    @PostMapping
    public ResponseEntity<Object> create(@Validated({Create.class}) @RequestBody NewCategoryDto newCategoryDto) {
        log.debug("Request accepted POST: /admin/categories");
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.create(newCategoryDto));
    }

    @PatchMapping
    public ResponseEntity<Object> update(@Validated(Update.class) @RequestBody CategoryDto categoryDto)
            throws ObjectNotFoundException {
        log.debug("Request accepted PATCH: /admin/categories");
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.update(categoryDto));
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Object> delete(@PathVariable("catId") long catId) throws ObjectNotFoundException {
        log.debug("Request accepted DELETE: /admin/categories/{}", catId);
        categoryService.delete(catId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
