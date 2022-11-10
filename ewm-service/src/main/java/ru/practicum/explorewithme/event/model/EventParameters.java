package ru.practicum.explorewithme.event.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import ru.practicum.explorewithme.exception.ValidException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
public class EventParameters {
    private String text;
    private List<Long> categories;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable;
    private PageRequest pageRequest;
    private EventSort eventSort;

    public EventParameters(String text, List<Long> categories, Boolean paid, String rangeStart,
                           String rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size)
            throws ValidException {
        this.text = text;
        this.categories = categories;
        this.paid = paid;
        if (rangeStart != null && rangeEnd != null) {
            this.rangeStart = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            this.rangeEnd = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } else {
            this.rangeStart = LocalDateTime.now();
            this.rangeEnd = LocalDateTime.now().plusYears(10);
        }
        this.onlyAvailable = onlyAvailable;
        this.pageRequest = PageRequest.of(from / size, size);
        try {
            EventSort thisSort = EventSort.valueOf(sort);
            if (thisSort == EventSort.EVENT_DATE) {
                this.eventSort = EventSort.EVENT_DATE;
            } else {
                this.eventSort = EventSort.VIEWS;
            }
        } catch (IllegalArgumentException e) {
            throw new ValidException("Unknown sort: " + sort);
        }

    }

}
