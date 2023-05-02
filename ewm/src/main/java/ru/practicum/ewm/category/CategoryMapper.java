package ru.practicum.ewm.category;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.UpdateEventUserRequest;

import java.util.Collection;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CategoryMapper {
    public static Category toCategory(NewCategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        return category;
    }

    public static Category toCategory(UpdateEventUserRequest.CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setId(categoryDto.getId());
        return category;
    }

    public static CategoryDto toCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }

    public static EventFullDto.CategoryDto toCategoryInnerDto(Category category) {
        EventFullDto.CategoryDto categoryDto = new EventFullDto.CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }

    public static EventShortDto.CategoryDto toCategoryShortInnerDto(Category category) {
        EventShortDto.CategoryDto categoryDto = new EventShortDto.CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }

    public static Collection<CategoryDto> toCategoryDto(Collection<Category> categories) {
        return categories.stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toUnmodifiableList());
    }
}
