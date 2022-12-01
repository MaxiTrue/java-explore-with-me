package ru.practicum.explorewithme.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmedRequestsDto {
    private Long eventId;
    private Long amountRequest;
}
