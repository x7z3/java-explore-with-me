package ru.practicum.ewm.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.comment.Comment;
import ru.practicum.ewm.comment.CommentMapper;
import ru.practicum.ewm.comment.CommentRepository;
import ru.practicum.ewm.comment.dto.CommentFullDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.enums.EventState;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;

import java.util.Collection;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private static final NotFoundException EVENT_NOT_FOUND_EXCEPTION = new NotFoundException("Event not found");
    private static final NotFoundException USER_NOT_FOUND_EXCEPTION = new NotFoundException("User not found");
    public static final NotFoundException COMMENT_NOT_FOUND_EXCEPTION = new NotFoundException("Comment not found");

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    public CommentFullDto addComment(Long userId, Long eventId, NewCommentDto commentDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> EVENT_NOT_FOUND_EXCEPTION);
        User user = userRepository.findById(userId).orElseThrow(() -> USER_NOT_FOUND_EXCEPTION);

        if (event.getState() == EventState.PENDING) {
            throw new ConflictException("Cannot add comment for event that is pending");
        }

        Comment comment = CommentMapper.toComment(commentDto, user, event);
        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.toCommentFullDto(savedComment);
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        if (userRepository.findById(userId).isEmpty()) throw USER_NOT_FOUND_EXCEPTION;
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> COMMENT_NOT_FOUND_EXCEPTION);

        if (!Objects.equals(comment.getAuthor().getId(), userId)) {
            throw COMMENT_NOT_FOUND_EXCEPTION;
        }
        commentRepository.delete(comment);
    }

    @Override
    public void deleteCommentByAdmin(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> COMMENT_NOT_FOUND_EXCEPTION);
        commentRepository.delete(comment);
    }

    @Override
    public CommentFullDto patchComment(Long userId, Long commentId, NewCommentDto commentDto) {
        if (userRepository.findById(userId).isEmpty()) throw USER_NOT_FOUND_EXCEPTION;
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> COMMENT_NOT_FOUND_EXCEPTION);
        if (!Objects.equals(comment.getAuthor().getId(), userId)) {
            throw COMMENT_NOT_FOUND_EXCEPTION;
        }
        comment.setText(commentDto.getText());
        return CommentMapper.toCommentFullDto(commentRepository.save(comment));
    }

    @Override
    public CommentFullDto patchCommentByAdmin(Long commentId, NewCommentDto commentDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> COMMENT_NOT_FOUND_EXCEPTION);
        comment.setText(commentDto.getText());
        return CommentMapper.toCommentFullDto(commentRepository.save(comment));
    }

    @Override
    public Collection<CommentFullDto> findCommentsByUserAndEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> EVENT_NOT_FOUND_EXCEPTION);
        User user = userRepository.findById(userId).orElseThrow(() -> USER_NOT_FOUND_EXCEPTION);
        return CommentMapper.toCommentFullDto(commentRepository.findByAuthorAndEvent(user, event));

    }

    @Override
    public CommentFullDto findCommentForAuthor(Long userId, Long commentId) {
        if (userRepository.findById(userId).isEmpty()) throw USER_NOT_FOUND_EXCEPTION;
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> COMMENT_NOT_FOUND_EXCEPTION);

        if (!Objects.equals(comment.getAuthor().getId(), userId)) {
            throw COMMENT_NOT_FOUND_EXCEPTION;
        }
        return CommentMapper.toCommentFullDto(comment);
    }

    @Override
    public Collection<CommentFullDto> findCommentsByEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> EVENT_NOT_FOUND_EXCEPTION);
        return CommentMapper.toCommentFullDto(commentRepository.findByEvent(event));
    }

    @Override
    public CommentFullDto findComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> COMMENT_NOT_FOUND_EXCEPTION);
        return CommentMapper.toCommentFullDto(comment);
    }
}


