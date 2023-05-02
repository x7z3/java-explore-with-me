package ru.practicum.ewm.compilation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventMapper;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CompilationMapper {
    public static CompilationDto toCompilationDto(Compilation compilation, Map<Long, Long> eventViews) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());
        compilationDto.setEvents(EventMapper.toEventShortInnerDto(compilation.getEvents(), eventViews));
        return compilationDto;
    }

    public static Collection<CompilationDto> toCompilationDto(Collection<Compilation> compilations,
                                                              Map<Long, Long> eventViews) {
        return compilations.stream()
                .map(compilation -> CompilationMapper.toCompilationDto(compilation, eventViews))
                .collect(Collectors.toUnmodifiableList());
    }

    public static Compilation toCompilation(NewCompilationDto compilationDto, Collection<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setTitle(compilationDto.getTitle());
        compilation.setPinned(compilationDto.getPinned());
        compilation.setEvents(events);
        return compilation;
    }
}