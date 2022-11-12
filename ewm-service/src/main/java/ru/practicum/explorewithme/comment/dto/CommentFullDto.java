package ru.practicum.explorewithme.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentFullDto {
    private Long id;
    private String text;
    private Long eventId;
    private Long commentatorId;
    private String commentDate;
    private Boolean byAdmin;
    private Boolean pinned;
    private Boolean changed;
}
