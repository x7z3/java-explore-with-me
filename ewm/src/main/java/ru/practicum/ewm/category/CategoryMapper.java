package ru.practicum.ewm.category;

import org.mapstruct.Mapper;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.UpdateEventUserRequest;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toCategory(NewCategoryDto category);

    Category toCategory(UpdateEventUserRequest.CategoryDto categoryDto);

    CategoryDto toCategoryDto(Category category);

    EventFullDto.CategoryDto toCategoryInnerDto(Category category);

    EventShortDto.CategoryDto toCategoryShortInnerDto(Category category);

    Collection<CategoryDto> toCategoryDto(Collection<Category> categories);

}
