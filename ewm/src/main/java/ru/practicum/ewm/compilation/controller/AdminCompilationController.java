package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.service.CompilationService;
import ru.practicum.ewm.util.ControllerLog;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
@Slf4j
public class AdminCompilationController {
    private final CompilationService compilationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<CompilationDto> findCompilations(
            HttpServletRequest request,
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        log.info("{}", ControllerLog.createUrlInfo(request));
        return compilationService.findCompilations(pinned, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(
            @RequestBody @Valid NewCompilationDto compilationDto,
            HttpServletRequest request
    ) {
        log.info("{}", ControllerLog.createUrlInfo(request));
        return compilationService.addCompilation(compilationDto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto patchCompilation(
            @PathVariable Long id,
            @RequestBody NewCompilationDto compilationDto,
            HttpServletRequest request
    ) {
        log.info("{}", ControllerLog.createUrlInfo(request));
        return compilationService.patchCompilation(id, compilationDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long id, HttpServletRequest request) {
        log.info("{}", ControllerLog.createUrlInfo(request));
        compilationService.deleteCompilation(id);
    }
}