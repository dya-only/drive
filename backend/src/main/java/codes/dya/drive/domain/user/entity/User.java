package codes.dya.drive.domain.user.entity;

import codes.dya.drive.domain.user.dto.UserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String email;
    private String name;
    private String password;

    private String image;

    public User(UserDto.Request userDto, String image) {
        this.email = userDto.getEmail();
        this.name = userDto.getName();
        this.password = userDto.getPassword();

        this.image = image;
    }
}