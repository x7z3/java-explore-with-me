package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.PaginationHelper;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserMapper;
import ru.practicum.ewm.user.UserRepository;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto addUser(final NewUserRequest userDto) {
        User user = UserMapper.toUser(userDto);
        UserDto newUser;
        try {
            newUser = UserMapper.toUserDto(userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Cannot create user.");
        }
        return newUser;
    }

    @Override
    public Collection<UserDto> findUsers(List<Long> ids, Integer from, Integer size) {
        Pageable pageable = PaginationHelper.makePageable(from, size);
        if (ids != null && !ids.isEmpty()) {
            return UserMapper.toUserDto(userRepository.findByIdIn(ids, pageable));
        } else {
            return UserMapper.toUserDto(userRepository.findAll(pageable).getContent());
        }
    }

    @Override
    public void deleteUser(Long id) {
        if (userRepository.findById(id).isEmpty()) throw new NotFoundException("User not found.");
        userRepository.deleteById(id);
    }
}

