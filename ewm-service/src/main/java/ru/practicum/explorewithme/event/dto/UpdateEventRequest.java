package ru.practicum.explorewithme.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.Update;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventRequest {
    @NotEmpty(groups = {Update.class})
    private String annotation;
    private Long category;
    @NotEmpty(groups = {Update.class})
    private String description;
    private String eventDate;
    @NotNull(groups = {Update.class})
    private Long eventId;
    private Boolean paid;
    private Long participantLimit;
    @NotEmpty(groups = {Update.class})
    private String title;
}
