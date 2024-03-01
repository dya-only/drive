package codes.dya.drive.domain.user.controller;

import codes.dya.drive.domain.user.dto.UserDto;
import codes.dya.drive.domain.user.service.UserService;
import codes.dya.drive.util.security.JwtService;
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
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<HttpStatus> createUser(@ModelAttribute UserDto.Request userDto) throws Exception {
        userService.create(userDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("@me")
    public ResponseEntity<?> findMe(@RequestHeader("Authorization") String token) throws NotFoundException {
        Long id = jwtService.getId(token);
        UserDto.Response userDto = userService.find(id);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> findUser(@PathVariable("id") Long id) throws NotFoundException {
        UserDto.Response userDto = userService.find(id);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    //TODO: Implement login controller
    @PostMapping("login")
    public ResponseEntity<?> login(@ModelAttribute UserDto.Login userDto) throws NotFoundException {

    }
}