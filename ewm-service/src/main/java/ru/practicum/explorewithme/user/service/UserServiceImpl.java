package ru.practicum.explorewithme.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.exception.ObjectNotFoundException;
import ru.practicum.explorewithme.user.dto.NewUserRequest;
import ru.practicum.explorewithme.user.dto.UserDto;
import ru.practicum.explorewithme.user.mapper.UserMapper;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> findByIds(List<Long> ids) {
        List<User> users = userStorage.findAllById(ids);
        return userMapper.toListResponseUserDto(users);
    }

    @Override
    public UserDto create(NewUserRequest newUserRequest) {
        User user = userMapper.toUserEntity(newUserRequest);
        return userMapper.toUserDto(userStorage.save(user));
    }

    @Override
    public void delete(long userId) throws ObjectNotFoundException {
        User user = userStorage.findById(userId).orElseThrow(() -> new ObjectNotFoundException(userId, "User"));
        userStorage.delete(user);
    }
}
