package ru.practicum.ewm.event;

import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryMapper;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.enums.EventState;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserMapper;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class EventMapper {
    private EventMapper() {
    }

    public static Event toEvent(User initiator, Category category, NewEventDto eventDto) {
        Event event = new Event();
        event.setAnnotation(eventDto.getAnnotation());
        event.setCategory(category);
        event.setDescription(eventDto.getDescription());
        event.setEventDate(eventDto.getEventDate());
        event.setLocation(eventDto.getLocation());
        event.setPaid(eventDto.getPaid());
        event.setParticipantLimit(eventDto.getParticipantLimit());
        event.setRequestModeration(eventDto.getRequestModeration());
        event.setTitle(eventDto.getTitle());
        event.setInitiator(initiator);
        event.setState(EventState.PENDING);
        return event;
    }

    public static EventFullDto toEventFullDto(Event event, Long views) {
        EventFullDto eventDto = new EventFullDto();
        eventDto.setId(event.getId());
        eventDto.setAnnotation(event.getAnnotation());
        eventDto.setCategory(CategoryMapper.toCategoryInnerDto(event.getCategory()));
        eventDto.setConfirmedRequests((long) event.getConfirmedRequests().size());
        eventDto.setCreatedOn(event.getCreatedOn());
        eventDto.setDescription(event.getDescription());
        eventDto.setEventDate(event.getEventDate());
        eventDto.setInitiator(UserMapper.toUserShortInnerDto(event.getInitiator()));
        eventDto.setLocation(event.getLocation());
        eventDto.setPaid(event.getPaid());
        eventDto.setParticipantLimit(event.getParticipantLimit());
        eventDto.setPublishedOn(event.getPublishedOn());
        eventDto.setRequestModeration(event.getRequestModeration());
        eventDto.setState(event.getState());
        eventDto.setTitle(event.getTitle());
        eventDto.setViews(views == null ? 0 : views);
        return eventDto;
    }

    public static EventShortDto toEventShortDto(Event event, Long views) {
        EventShortDto eventDto = new EventShortDto();
        eventDto.setId(event.getId());
        eventDto.setAnnotation(event.getAnnotation());
        eventDto.setCategory(CategoryMapper.toCategoryShortInnerDto(event.getCategory()));
        eventDto.setConfirmedRequests((long) event.getConfirmedRequests().size());
        eventDto.setEventDate(event.getEventDate());
        eventDto.setInitiator(UserMapper.toUserEventShortInnerDto(event.getInitiator()));
        eventDto.setPaid(event.getPaid());
        eventDto.setTitle(event.getTitle());
        eventDto.setViews(views == null ? 0 : views);
        return eventDto;
    }

    public static CompilationDto.EventInnerShortDto toEventShortInnerDto(Event event, Long views) {
        CompilationDto.EventInnerShortDto eventDto = new CompilationDto.EventInnerShortDto();
        eventDto.setId(event.getId());
        eventDto.setAnnotation(event.getAnnotation());
        eventDto.setCategory(CategoryMapper.toCategoryShortInnerDto(event.getCategory()));
        eventDto.setConfirmedRequests((long) event.getConfirmedRequests().size());
        eventDto.setEventDate(event.getEventDate());
        eventDto.setInitiator(UserMapper.toUserEventShortInnerDto(event.getInitiator()));
        eventDto.setPaid(event.getPaid());
        eventDto.setTitle(event.getTitle());
        eventDto.setViews(views == null ? 0 : views);
        return eventDto;
    }

    public static Collection<EventFullDto> toEventFullDto(Iterable<Event> events, Map<Long, Long> views) {
        return StreamSupport.stream(events.spliterator(), false)
                .map(event -> EventMapper.toEventFullDto(event, views.get(event.getId())))
                .collect(Collectors.toUnmodifiableList());
    }

    public static EventFullDto toEventFullDto(Event event, Map<Long, Long> views) {
        return toEventFullDto(event, views.get(event.getId()));
    }

    public static Collection<EventShortDto> toEventShortDto(Collection<Event> events, Map<Long, Long> views) {
        return events.stream()
                .map(event -> EventMapper.toEventShortDto(event, views.get(event.getId())))
                .collect(Collectors.toUnmodifiableList());
    }

    public static Collection<CompilationDto.EventInnerShortDto> toEventShortInnerDto(Collection<Event> events, Map<Long, Long> views) {
        return events.stream()
                .map(event -> EventMapper.toEventShortInnerDto(event, views.get(event.getId())))
                .collect(Collectors.toUnmodifiableList());
    }
}
