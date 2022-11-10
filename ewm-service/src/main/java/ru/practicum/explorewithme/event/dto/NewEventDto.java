package ru.practicum.explorewithme.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.Create;
import ru.practicum.explorewithme.location.dto.LocationDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotEmpty(groups = {Create.class})
    @NotNull(groups = {Create.class})
    private String annotation;
    @NotNull(groups = {Create.class})
    private Long category;
    @NotEmpty(groups = {Create.class})
    @NotNull(groups = {Create.class})
    private String description;
    @NotEmpty(groups = {Create.class})
    @NotNull(groups = {Create.class})
    private String eventDate;
    @NotNull(groups = {Create.class})
    private LocationDto location;
    private Boolean paid;
    private Long participantLimit;
    private Boolean requestModeration;
    @NotEmpty(groups = {Create.class})
    @NotNull(groups = {Create.class})
    private String title;
}
