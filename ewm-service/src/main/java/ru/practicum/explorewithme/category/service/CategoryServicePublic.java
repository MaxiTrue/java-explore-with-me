package ru.practicum.explorewithme.category.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.exception.ObjectNotFoundException;

import java.util.List;

public interface CategoryServicePublic {

    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto findById(long catId) throws ObjectNotFoundException;

}
