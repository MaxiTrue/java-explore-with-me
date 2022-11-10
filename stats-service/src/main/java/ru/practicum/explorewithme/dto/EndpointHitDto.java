package ru.practicum.explorewithme.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndpointHitDto {
    @NotNull
    @NotEmpty
    private String app;
    @NotNull
    @NotEmpty
    private String uri;
    @NotNull
    @NotEmpty
    private String ip;
    @NotNull
    @NotEmpty
    private String timestamp;
}
