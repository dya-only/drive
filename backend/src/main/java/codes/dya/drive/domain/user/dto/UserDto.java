package codes.dya.drive.domain.user.dto;

import codes.dya.drive.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

public class UserDto {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Login {
        private String email;
        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request {
        String email;
        String name;
        String password;

        MultipartFile image;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        int id;
        String email;
        String name;
        String image;

        public Response(User user) {
            this.id = user.getId();
            this.email = user.getEmail();
            this.name = user.getName();
            this.image = user.getImage();
        }
    }
}