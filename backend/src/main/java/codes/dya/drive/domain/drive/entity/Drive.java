package codes.dya.drive.domain.drive.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Drive {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String originalName;
    private String fileName;
    private String size;
    private String type;

    public Drive(String originalName, String fileName, String size, String type) {
        this.originalName = originalName;
        this.fileName = fileName;
        this.size = size;
        this.type = type;
    }
}