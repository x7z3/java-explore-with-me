package ru.practicum.ewm.compilation.service;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;

import java.util.Collection;

public interface CompilationService {
    Collection<CompilationDto> findCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto addCompilation(NewCompilationDto compilationDto);

    void deleteCompilation(Long id);

    CompilationDto findCompilation(Long id);

    CompilationDto patchCompilation(Long id, NewCompilationDto compilationDto);
}

