package ru.practicum.explorewithme.location.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.location.model.Location;

import java.util.Optional;

public interface LocationStorage extends JpaRepository<Location, Long> {

    Optional<Location> findLocationByLatAndLon(double lat, double lon);

}
