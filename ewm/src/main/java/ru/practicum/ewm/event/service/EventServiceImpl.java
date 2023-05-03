package ru.practicum.ewm.event.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.PaginationHelper;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryMapper;
import ru.practicum.ewm.category.CategoryRepository;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventMapper;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.QEvent;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.enums.EventSort;
import ru.practicum.ewm.event.enums.EventState;
import ru.practicum.ewm.event.enums.StateAdminAction;
import ru.practicum.ewm.event.enums.StateUserAction;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.EwmException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.request.ParticipationRequest;
import ru.practicum.ewm.request.RequestMapper;
import ru.practicum.ewm.request.RequestRepository;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.enums.ParticipationRequestStatus;
import ru.practicum.ewm.request.enums.ParticipationRequestStatusUpdate;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;
import ru.practicum.statistics.client.StatisticsRestClient;
import ru.practicum.statistics.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    public static final NotFoundException EVENT_NOT_FOUND_EXCEPTION = new NotFoundException("Event not found");
    public static final NotFoundException USER_NOT_FOUND_EXCEPTION = new NotFoundException("User not found");
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final StatisticsRestClient statsClient;

    private final ConflictException conflictExceptionIncorrectEventTime = new ConflictException("Incorrect event time");

    @Override
    public Map<Long, Long> getViews(Collection<Event> events) {

        List<String> uris = events.stream()
                .map(Event::getId)
                .map(id -> "/events/" + id.toString())
                .collect(Collectors.toUnmodifiableList());

        List<ViewStatsDto> eventStats = statsClient.getStats(uris);

        return eventStats.stream()
                .filter(statRecord -> statRecord.getApp().equals("ewm-service"))
                .collect(Collectors.toMap(
                                statRecord -> {
                                    Pattern pattern = Pattern.compile("/events/(\\d*)");
                                    Matcher matcher = pattern.matcher(statRecord.getUri());
                                    return Long.parseLong(matcher.group(1));
                                },
                                ViewStatsDto::getHits
                        )
                );
    }

    @Override
    public EventFullDto addEvent(final Long userId, final NewEventDto eventDto) {
        if (eventDto.getEventDate() != null
                && eventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw conflictExceptionIncorrectEventTime;
        }
        User initiator = userRepository.findById(userId).orElseThrow(RuntimeException::new);
        Category category = categoryRepository.findById(eventDto.getCategory()).orElseThrow(RuntimeException::new);
        Event event = EventMapper.toEvent(initiator, category, eventDto);
        return EventMapper.toEventFullDto(eventRepository.save(event), 0L);
    }

    @Override
    public Collection<EventFullDto> findEvents(
            Long[] users,
            String[] states,
            Long[] categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Integer from,
            Integer size
    ) {
        Pageable pageable = PaginationHelper.makePageable(from, size);
        QEvent qEvent = QEvent.event;
        BooleanExpression expression = qEvent.id.isNotNull();
        if (users != null && users.length > 0) {
            expression = expression.and(qEvent.initiator.id.in(users));
        }
        if (states != null && states.length > 0) {
            expression = expression.and(qEvent.state.in(Arrays.stream(states)
                    .map(EventState::valueOf)
                    .collect(Collectors.toUnmodifiableList())));
        }
        if (categories != null && categories.length > 0) {
            expression = expression.and(qEvent.category.id.in(categories));
        }
        if (rangeStart != null) {
            expression = expression.and(qEvent.eventDate.goe(rangeStart));
        }
        if (rangeEnd != null) {
            expression = expression.and(qEvent.eventDate.loe(rangeEnd));
        }

        Collection<Event> events = eventRepository.findAll(expression, pageable).getContent();

        Map<Long, Long> views = this.getViews(events);

        return EventMapper.toEventFullDto(events, views);
    }

    @Override
    public Collection<EventShortDto> findEvents(
            String text,
            Boolean paid,
            Boolean onlyAvailable,
            EventSort sort,
            Long[] categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Integer from,
            Integer size
    ) {
        Pageable pageable = PaginationHelper.makePageable(from, size);
        QEvent qEvent = QEvent.event;
        BooleanExpression expression = qEvent.state.eq(EventState.PUBLISHED);
        if (text != null) {
            expression = expression.and(qEvent.annotation.containsIgnoreCase(text).or(qEvent.description.containsIgnoreCase(text)));
        }
        if (paid != null) {
            expression = expression.and(qEvent.paid.eq(paid));
        }
        if (categories != null && categories.length > 0) {
            expression = expression.and(qEvent.category.id.in(categories));
        }
        if (rangeStart != null) {
            expression = expression.and(qEvent.eventDate.goe(rangeStart));
        }
        if (rangeEnd != null) {
            expression = expression.and(qEvent.eventDate.loe(rangeEnd));
        }

        Collection<Event> events = eventRepository.findAll(expression, pageable).getContent();
        return EventMapper.toEventShortDto(events, this.getViews(events));
    }

    @Override
    public EventFullDto patchEventByInitiator(Long userId, Long eventId, UpdateEventUserRequest updateRequest) {
        if (userRepository.findById(userId).isEmpty()) throw new EwmException();
        Event eventToUpdate = eventRepository.findById(eventId).orElseThrow(EwmException::new);

        if (
                updateRequest.getEventDate() != null
                        && updateRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw conflictExceptionIncorrectEventTime;
        }
        if (eventToUpdate.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Cannot update published event");
        }

        if (!userId.equals(eventToUpdate.getInitiator().getId())) {
            throw EVENT_NOT_FOUND_EXCEPTION;
        }
        if (updateRequest.getAnnotation() != null) {
            eventToUpdate.setAnnotation(updateRequest.getAnnotation());
        }
        if (updateRequest.getCategory() != null) {
            eventToUpdate.setCategory(CategoryMapper.toCategory(updateRequest.getCategory()));
        }
        if (updateRequest.getDescription() != null) {
            eventToUpdate.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getEventDate() != null) {
            eventToUpdate.setEventDate(updateRequest.getEventDate());
        }
        if (updateRequest.getLocation() != null) {
            eventToUpdate.setPaid(updateRequest.getPaid());
        }
        if (updateRequest.getParticipantLimit() != null) {
            eventToUpdate.setParticipantLimit(updateRequest.getParticipantLimit());
        }
        if (updateRequest.getRequestModeration() != null) {
            eventToUpdate.setRequestModeration(updateRequest.getRequestModeration());
        }
        if (updateRequest.getStateAction() != null) {
            if (updateRequest.getStateAction() == StateUserAction.SEND_TO_REVIEW) {
                eventToUpdate.setState(EventState.PENDING);
            } else if (updateRequest.getStateAction() == StateUserAction.CANCEL_REVIEW) {
                eventToUpdate.setState(EventState.CANCELED);
            }
        }
        if (updateRequest.getTitle() != null) {
            eventToUpdate.setTitle(updateRequest.getTitle());
        }

        Event updatedEvent = eventRepository.save(eventToUpdate);

        return EventMapper.toEventFullDto(updatedEvent, this.getViews(List.of(updatedEvent)));
    }

    @Override
    public EventFullDto patchEventByAdmin(Long eventId, UpdateEventAdminRequest updateRequest) {
        Event eventToUpdate = eventRepository.findById(eventId)
                .orElseThrow(() -> EVENT_NOT_FOUND_EXCEPTION);

        if (updateRequest.getEventDate() != null
                && updateRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw conflictExceptionIncorrectEventTime;
        }

        if (updateRequest.getStateAction() != null) {
            if (updateRequest.getStateAction() == StateAdminAction.PUBLISH_EVENT
                    && eventToUpdate.getState() != EventState.PENDING
            ) {
                throw new ConflictException("Conflicting state action");
            }
            if (updateRequest.getStateAction() == StateAdminAction.REJECT_EVENT
                    && eventToUpdate.getState() == EventState.PUBLISHED) {
                throw new ConflictException("Cannot reject published event");
            }
        }

        if (updateRequest.getAnnotation() != null) {
            eventToUpdate.setAnnotation(updateRequest.getAnnotation());
        }
        if (updateRequest.getCategory() != null) {
            Category category = categoryRepository.findById(updateRequest.getCategory()).orElseThrow(() -> new NotFoundException("Category not found"));
            eventToUpdate.setCategory(category);
        }
        if (updateRequest.getDescription() != null) {
            eventToUpdate.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getEventDate() != null) {
            eventToUpdate.setEventDate(updateRequest.getEventDate());
        }
        if (updateRequest.getLocation() != null) {
            eventToUpdate.setPaid(updateRequest.getPaid());
        }
        if (updateRequest.getParticipantLimit() != null) {
            eventToUpdate.setParticipantLimit(updateRequest.getParticipantLimit());
        }
        if (updateRequest.getRequestModeration() != null) {
            eventToUpdate.setRequestModeration(updateRequest.getRequestModeration());
        }
        if (updateRequest.getStateAction() != null) {
            if (updateRequest.getStateAction() == StateAdminAction.PUBLISH_EVENT) {
                eventToUpdate.setState(EventState.PUBLISHED);
            } else if (updateRequest.getStateAction() == StateAdminAction.REJECT_EVENT) {
                eventToUpdate.setState(EventState.CANCELED);
            }
        }
        if (updateRequest.getTitle() != null) {
            eventToUpdate.setTitle(updateRequest.getTitle());
        }

        Event updatedEvent = eventRepository.save(eventToUpdate);

        return EventMapper.toEventFullDto(updatedEvent, this.getViews(List.of(updatedEvent)));
    }

    @Override
    public EventRequestStatusUpdateResult changeRequestStatus(
            Long userId,
            Long eventId,
            EventRequestStatusUpdateRequest statusUpdateRequest
    ) {
        if (userRepository.findById(userId).isEmpty()) throw new EwmException();
        Event event = eventRepository.findById(eventId).orElseThrow(RuntimeException::new);
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        for (Long requestId :
                statusUpdateRequest.getRequestIds()) {
            ParticipationRequest request = requestRepository.findById(requestId).orElseThrow(RuntimeException::new);
            if (request.getStatus() != ParticipationRequestStatus.PENDING) {
                throw new ConflictException("Cannot change request status");
            }
            if (statusUpdateRequest.getStatus() == ParticipationRequestStatusUpdate.CONFIRMED) {
                if ((event.getParticipantLimit() == 0 || !event.getRequestModeration())
                        && event.getParticipantLimit() > event.getRequests().size()) {
                    saveConfirmedRequest(result, request);
                } else if (
                        !event.getRequestModeration() && event.getParticipantLimit() > 0
                                && event.getParticipantLimit() > event.getConfirmedRequests().size()
                ) {
                    saveConfirmedRequest(result, request);
                } else if (event.getConfirmedRequests().size() < event.getParticipantLimit()) {
                    saveConfirmedRequest(result, request);
                } else {
                    request.setStatus(ParticipationRequestStatus.REJECTED);
                    requestRepository.save(request);
                    List<ParticipationRequest> allPendingRequestsLeftUnprocessed = requestRepository.findByEventAndStatus(event, ParticipationRequestStatus.PENDING);
                    for (ParticipationRequest pendingRequest : allPendingRequestsLeftUnprocessed) {
                        pendingRequest.setStatus(ParticipationRequestStatus.REJECTED);
                        requestRepository.save(pendingRequest);
                    }
                    throw new ConflictException("The participant limit has been reached");
                }
            } else {
                request.setStatus(ParticipationRequestStatus.REJECTED);
                EventRequestStatusUpdateResult.ParticipationRequestDto requestDto =
                        RequestMapper.toRequestInnerDto(requestRepository.save(request));
                result.getRejectedRequests().add(requestDto);
            }
        }
        return result;
    }

    private void saveConfirmedRequest(EventRequestStatusUpdateResult result, ParticipationRequest request) {
        request.setStatus(ParticipationRequestStatus.CONFIRMED);
        EventRequestStatusUpdateResult.ParticipationRequestDto requestDto =
                RequestMapper.toRequestInnerDto(requestRepository.save(request));
        result.getConfirmedRequests().add(requestDto);
    }

    @Override
    public EventFullDto findEvent(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> EVENT_NOT_FOUND_EXCEPTION);
        Map<Long, Long> views = getViews(List.of(event));
        return EventMapper.toEventFullDto(event, views);
    }

    @Override
    public Collection<ParticipationRequestDto> findEventRequests(Long userId, Long eventId) {
        if (userRepository.findById(userId).isEmpty()) throw USER_NOT_FOUND_EXCEPTION;
        Event event = eventRepository.findById(eventId).orElseThrow(() -> EVENT_NOT_FOUND_EXCEPTION);
        if (!event.getInitiator().getId().equals(userId)) {
            throw EVENT_NOT_FOUND_EXCEPTION;
        }
        return RequestMapper.toRequestDto(event.getRequests());
    }

    @Override
    public Collection<EventFullDto> findEvents(Long userId, Integer from, Integer size) {
        Pageable pageable = PaginationHelper.makePageable(from, size);
        Collection<Event> events = eventRepository.findByInitiatorId(userId, pageable);
        return EventMapper.toEventFullDto(events, this.getViews(events));
    }
}