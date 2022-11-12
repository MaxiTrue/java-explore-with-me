package ru.practicum.explorewithme.user.service;

import ru.practicum.explorewithme.exception.ObjectNotFoundException;
import ru.practicum.explorewithme.user.dto.NewUserRequest;
import ru.practicum.explorewithme.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> findByIds(List<Long> ids);

    UserDto create(NewUserRequest newUserRequest);

    void delete(long userId) throws ObjectNotFoundException;
}
