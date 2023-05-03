package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentFullDto;
import ru.practicum.ewm.comment.service.CommentService;
import ru.practicum.ewm.util.ControllerLog;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/comments")
@Slf4j
public class PublicCommentController {
    private final CommentService commentService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<CommentFullDto> findComments(@RequestParam Long eventId, HttpServletRequest request) {
        log.info("{}", ControllerLog.createUrlInfo(request));
        return commentService.findCommentsByEvent(eventId);
    }

    @GetMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentFullDto findComment(@PathVariable Long commentId, HttpServletRequest request) {
        log.info("{}", ControllerLog.createUrlInfo(request));
        return commentService.findComment(commentId);
    }
}
