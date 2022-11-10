package ru.practicum.explorewithme.user.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.Create;
import ru.practicum.explorewithme.exception.ObjectNotFoundException;
import ru.practicum.explorewithme.user.dto.NewUserRequest;
import ru.practicum.explorewithme.user.service.UserService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("admin/users")
@RequiredArgsConstructor
public class UserControllerAdmin {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Object> findByIds(@RequestParam("ids") List<Long> ids) {
        log.debug("Request accepted GET: admin/users");
        return ResponseEntity.status(HttpStatus.OK).body(userService.findByIds(ids));
    }

    @PostMapping
    public ResponseEntity<Object> create(@Validated({Create.class}) @RequestBody NewUserRequest newUserRequest) {
        log.debug("Request accepted POST: admin/users");
        return ResponseEntity.status(HttpStatus.OK).body(userService.create(newUserRequest));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@PathVariable("userId") long userId) throws ObjectNotFoundException {
        log.debug("Request accepted DELETE: admin/users/{userId}");
        userService.delete(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
