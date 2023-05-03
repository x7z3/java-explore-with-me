package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentFullDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.service.CommentService;
import ru.practicum.ewm.util.ControllerLog;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/comments")
@Slf4j
public class AdminCommentController {
    private final CommentService commentService;

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(@PathVariable Long commentId, HttpServletRequest request) {
        log.info("{}", ControllerLog.createUrlInfo(request));
        commentService.deleteCommentByAdmin(commentId);
    }

    @PatchMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public CommentFullDto patchComment(
            @PathVariable Long commentId,
            @RequestBody @Valid NewCommentDto commentDto,
            HttpServletRequest request
    ) {
        log.info("{}", ControllerLog.createUrlInfo(request));
        return commentService.patchCommentByAdmin(commentId, commentDto);
    }
}
