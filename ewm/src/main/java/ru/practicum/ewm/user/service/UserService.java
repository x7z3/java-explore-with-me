package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.Collection;
import java.util.List;

public interface UserService {
    UserDto addUser(final NewUserRequest user);

    Collection<UserDto> findUsers(List<Long> ids, Integer from, Integer size);

    void deleteUser(Long id);
}
