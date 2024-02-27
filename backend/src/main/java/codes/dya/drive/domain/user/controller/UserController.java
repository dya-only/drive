package codes.dya.drive.domain.user.controller;

import codes.dya.drive.domain.user.dto.UserDto;
import codes.dya.drive.domain.user.service.UserService;
import com.amazonaws.services.kms.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<HttpStatus> createUser(@ModelAttribute UserDto.Request userDto) throws Exception {
        userService.create(userDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> findUser(@PathVariable("id") Long id) throws NotFoundException {
        UserDto.Response userDto = userService.find(id);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}