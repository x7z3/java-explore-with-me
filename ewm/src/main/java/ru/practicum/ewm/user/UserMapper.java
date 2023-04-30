package ru.practicum.ewm.user;

import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.util.Collection;
import java.util.stream.Collectors;

public final class UserMapper {
    private UserMapper() {
    }

    public static User toUser(NewUserRequest userRequest) {
        User user = new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        return user;
    }

    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public static UserShortDto toUserShortDto(User user) {
        UserShortDto userDto = new UserShortDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        return userDto;
    }

    public static EventFullDto.UserShortDto toUserShortInnerDto(User user) {
        EventFullDto.UserShortDto userDto = new EventFullDto.UserShortDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        return userDto;
    }

    public static EventShortDto.UserShortDto toUserEventShortInnerDto(User user) {
        EventShortDto.UserShortDto userDto = new EventShortDto.UserShortDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        return userDto;
    }

    public static Collection<UserDto> toUserDto(Collection<User> users) {
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toUnmodifiableList());
    }
}
