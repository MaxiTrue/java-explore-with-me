package ru.practicum.explorewithme.compilation.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.compilation.model.Compilation;

public interface CompilationStorage extends JpaRepository<Compilation, Long> {

    Page<Compilation> findAllByPinned(boolean pinned, Pageable pageable);

}
