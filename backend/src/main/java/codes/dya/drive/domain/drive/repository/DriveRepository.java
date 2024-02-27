package codes.dya.drive.domain.drive.repository;

import codes.dya.drive.domain.drive.entity.Drive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriveRepository extends JpaRepository<Drive, String> {

    Optional<Drive> findByFileName(String fileName);
}
