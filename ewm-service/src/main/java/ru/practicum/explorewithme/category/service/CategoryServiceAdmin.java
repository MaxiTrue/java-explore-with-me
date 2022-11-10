package ru.practicum.explorewithme.category.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.dto.NewCategoryDto;
import ru.practicum.explorewithme.exception.ObjectNotFoundException;

import java.util.List;

public interface CategoryServiceAdmin {

    CategoryDto create(NewCategoryDto newCategoryDto);

    CategoryDto update(CategoryDto categoryDto) throws ObjectNotFoundException;

    void delete(long catId) throws ObjectNotFoundException;

}
