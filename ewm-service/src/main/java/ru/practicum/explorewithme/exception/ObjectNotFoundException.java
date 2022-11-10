package ru.practicum.explorewithme.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ObjectNotFoundException extends Exception {

    private final Long objectId;
    private final String objectName;

}
