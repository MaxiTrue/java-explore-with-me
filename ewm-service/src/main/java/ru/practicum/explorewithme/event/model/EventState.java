package ru.practicum.explorewithme.event.model;

import java.util.Optional;

public enum EventState {
    PENDING,   // ожидающие
    PUBLISHED, // опубликованные
    CANCELED;  // отклонённые

    public static Optional<EventState> from(String stringState) {
        for (EventState stateEvent : values()) {
            if (stateEvent.name().equalsIgnoreCase(stringState)) {
                return Optional.of(stateEvent);
            }
        }
        return Optional.empty();
    }
}
