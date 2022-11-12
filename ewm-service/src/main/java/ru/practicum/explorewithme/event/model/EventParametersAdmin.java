package ru.practicum.explorewithme.event.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class EventParametersAdmin {

    private List<Long> users;
    private List<Long> categories;
    private List<EventState> states;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private PageRequest pageRequest;

    public EventParametersAdmin(List<Long> users, List<Long> categories, List<String> states,
                                String rangeStart, String rangeEnd, Integer from, Integer size) {
        this.users = users;
        this.categories = categories;
        this.states = states != null ? states.stream().map(EventState::valueOf).collect(Collectors.toList()) : null;

        if (rangeStart != null && rangeEnd != null) {
            this.rangeStart = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            this.rangeEnd = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } else {
            this.rangeStart = LocalDateTime.now();
            this.rangeEnd = LocalDateTime.now().plusYears(10);
        }

        this.pageRequest = PageRequest.of(from / size, size);
    }
}
