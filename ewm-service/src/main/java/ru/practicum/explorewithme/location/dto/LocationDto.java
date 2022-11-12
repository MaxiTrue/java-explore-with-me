package ru.practicum.explorewithme.location.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.constaints.Create;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
    @NotNull(groups = {Create.class})
    private Double lat;
    @NotNull(groups = {Create.class})
    private Double lon;
}
