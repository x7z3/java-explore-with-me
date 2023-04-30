package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.PaginationHelper;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryMapper;
import ru.practicum.ewm.category.CategoryRepository;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private static final NotFoundException CATEGORY_NOT_FOUND_EXCEPTION = new NotFoundException("Category not found.");
    private static final ConflictException NAME_IS_NOT_UNIQUE_CONFLICT_EXCEPTION = new ConflictException("Name is not unique.");
    public static final ConflictException LINKED_EVENTS_EXISTING_EXCEPTION = new ConflictException("Cannot delete category with linked events.");
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto addCategory(final NewCategoryDto categoryDto) {
        Category category = CategoryMapper.toCategory(categoryDto);
        Category createdCategory;
        try {
            createdCategory = categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw NAME_IS_NOT_UNIQUE_CONFLICT_EXCEPTION;
        }
        return CategoryMapper.toCategoryDto(createdCategory);
    }

    @Override
    public Collection<CategoryDto> findCategories(Integer from, Integer size) {
        Pageable pageable = PaginationHelper.makePageable(from, size);
        return CategoryMapper.toCategoryDto(categoryRepository.findAll(pageable).getContent());
    }

    @Override
    public CategoryDto patchCategory(Long id, NewCategoryDto categoryDto) {
        Category categoryToUpdate = categoryRepository.findById(id)
                .orElseThrow(() -> CATEGORY_NOT_FOUND_EXCEPTION);

        categoryToUpdate.setName(categoryDto.getName());

        Category updatedCategory;
        try {
            updatedCategory = categoryRepository.save(categoryToUpdate);
        } catch (DataIntegrityViolationException e) {
            throw NAME_IS_NOT_UNIQUE_CONFLICT_EXCEPTION;
        }
        return CategoryMapper.toCategoryDto(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(()
                -> CATEGORY_NOT_FOUND_EXCEPTION);
        if (eventRepository.countByCategoryId(id) > 0) {
            throw LINKED_EVENTS_EXISTING_EXCEPTION;
        }
        categoryRepository.delete(category);
    }

    @Override
    public CategoryDto findCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(()
                -> CATEGORY_NOT_FOUND_EXCEPTION);
        return CategoryMapper.toCategoryDto(category);
    }
}
