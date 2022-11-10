package ru.practicum.explorewithme.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.Create;
import ru.practicum.explorewithme.Update;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCategoryDto {
    @NotNull(groups = {Create.class, Update.class})
    @NotEmpty(groups = {Create.class, Update.class})
    @Size(groups = {Create.class, Update.class}, min = 3, max = 100)
    private String name;
}
