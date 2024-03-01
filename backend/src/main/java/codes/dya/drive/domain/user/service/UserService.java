package codes.dya.drive.domain.user.service;

import codes.dya.drive.domain.user.dto.UserDto;
import codes.dya.drive.domain.user.entity.User;
import codes.dya.drive.domain.user.repository.UserRepository;
import codes.dya.drive.util.image.Image;
import codes.dya.drive.util.image.ImageUploader;
import com.amazonaws.services.kms.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ImageUploader imageUploader;

    public void create(UserDto.Request userDto) throws Exception {
        Optional<User> _user = userRepository.findByEmail(userDto.getEmail());
        if (_user.isPresent())
            throw new Exception("User already exists");

        String encryptedPassword = BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt());
        userDto.setPassword(encryptedPassword);

        if (userDto.getImage() == null) {
            User user = new User(userDto, "default.png");
            userRepository.save(user);
            return;
        }

        Image image = imageUploader.upload(userDto.getImage(), "profile");
        User user = new User(userDto, image.getImageName());
        userRepository.save(user);
    }

    public UserDto.Response find(Long id) throws NotFoundException {
        return new UserDto.Response(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found")));
    }

    //TODO: Implement loginAndGetToken service
    public String loginAndGetToken(UserDto.Login userDto) {
    }
}
