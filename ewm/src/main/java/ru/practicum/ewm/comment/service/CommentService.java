package ru.practicum.ewm.comment.service;

import ru.practicum.ewm.comment.dto.CommentFullDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;

import java.util.Collection;

public interface CommentService {
    CommentFullDto addComment(Long userId, Long eventId, NewCommentDto commentDto);

    void deleteComment(Long userId, Long commentId);

    void deleteCommentByAdmin(Long commentId);

    CommentFullDto patchComment(Long userId, Long commentId, NewCommentDto commentDto);

    CommentFullDto patchCommentByAdmin(Long commentId, NewCommentDto commentDto);

    Collection<CommentFullDto> findCommentsByUserAndEvent(Long userId, Long eventId);

    CommentFullDto findCommentForAuthor(Long userId, Long commentId);

    Collection<CommentFullDto> findCommentsByEvent(Long eventId);

    CommentFullDto findComment(Long commentId);
}