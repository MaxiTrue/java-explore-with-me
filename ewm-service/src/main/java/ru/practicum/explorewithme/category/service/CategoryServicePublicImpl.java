package ru.practicum.explorewithme.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.mapper.CategoryMapper;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.storage.CategoryStorage;
import ru.practicum.explorewithme.exception.ObjectNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServicePublicImpl implements CategoryServicePublic {

    private final CategoryStorage categoryStorage;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return categoryStorage.findAll(pageable).stream()
                .map(categoryMapper::toCategoryDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto findById(long catId) throws ObjectNotFoundException {
        Category category = categoryStorage.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException(catId, "Category"));
        return categoryMapper.toCategoryDto(category);
    }

}
