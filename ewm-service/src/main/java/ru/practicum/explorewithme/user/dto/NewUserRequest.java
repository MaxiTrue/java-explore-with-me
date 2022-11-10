package ru.practicum.explorewithme.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.Create;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
    @NotBlank(groups = {Create.class})
    @NotNull(groups = {Create.class})
    @Size(groups = {Create.class}, min = 3, max = 100)
    private String name;
    @NotBlank(groups = {Create.class})
    @NotNull(groups = {Create.class})
    @Email(groups = {Create.class})
    private String email;
}
