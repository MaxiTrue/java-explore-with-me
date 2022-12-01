package ru.practicum.explorewithme.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCommentDto {
    @NotBlank
    @Size(min = 2, max = 5000)
    private String text;
    private Boolean byAdmin;
    private Boolean pinned;
    private Boolean changed;
}
