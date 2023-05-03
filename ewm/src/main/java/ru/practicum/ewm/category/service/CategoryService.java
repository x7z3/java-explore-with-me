package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;

import java.util.Collection;

public interface CategoryService {
    CategoryDto addCategory(final NewCategoryDto category);

    Collection<CategoryDto> findCategories(Integer from, Integer size);

    CategoryDto patchCategory(Long id, NewCategoryDto categoryDto);

    void deleteCategory(Long id);

    CategoryDto findCategory(Long id);
}
