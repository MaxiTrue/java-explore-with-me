package ru.practicum.explorewithme.request.model;


import java.util.Optional;

public enum StatusRequest {

    PENDING,   // ожидает
    CONFIRMED, // подтверждён
    REJECTED,  // отклонён админом
    CANCELED;  // отменён автором

    public static Optional<StatusRequest> from(String stringState) {
        for (StatusRequest statusRequest : values()) {
            if (statusRequest.name().equalsIgnoreCase(stringState)) {
                return Optional.of(statusRequest);
            }
        }
        return Optional.empty();
    }
}
