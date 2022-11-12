package ru.practicum.explorewithme.category.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.category.model.Category;

public interface CategoryStorage extends JpaRepository<Category, Long> {
}
