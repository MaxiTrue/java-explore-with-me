package ru.practicum.explorewithme.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.dto.NewCategoryDto;
import ru.practicum.explorewithme.category.mapper.CategoryMapper;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.storage.CategoryStorage;
import ru.practicum.explorewithme.exception.ObjectNotFoundException;

@Service
@RequiredArgsConstructor
public class CategoryServiceAdminImpl implements CategoryServiceAdmin {

    private final CategoryStorage categoryStorage;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto create(NewCategoryDto categoryDto) {
        Category category = categoryMapper.toCategory(categoryDto);
        return categoryMapper.toCategoryDto(categoryStorage.save(category));
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto) throws ObjectNotFoundException {
        Category category = categoryStorage.findById(categoryDto.getId())
                .orElseThrow(() -> new ObjectNotFoundException(categoryDto.getId(), "Category"));
        category.setName(categoryDto.getName());
        Category categoryFromStorage = categoryStorage.save(category);
        return categoryMapper.toCategoryDto(categoryFromStorage);
    }

    @Override
    public void delete(long catId) throws ObjectNotFoundException {
        Category category = categoryStorage.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException(catId, "Category"));
        categoryStorage.delete(category);
    }

}
