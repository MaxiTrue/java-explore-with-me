package ru.practicum.explorewithme.user.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.user.model.User;

public interface UserStorage extends JpaRepository<User, Long> {
}
