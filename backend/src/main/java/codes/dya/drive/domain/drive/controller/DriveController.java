package codes.dya.drive.domain.drive.controller;

import codes.dya.drive.domain.drive.dto.DriveDto;
import codes.dya.drive.domain.drive.service.DriveService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/drive")
@RequiredArgsConstructor
public class DriveController {

    private final DriveService driveService;

    @PostMapping
    public ResponseEntity<HttpStatus> UploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        driveService.upload(file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> findById(@PathVariable("id") String id) throws NotFoundException {
        DriveDto.Response driveDto = driveService.find(id);
        return new ResponseEntity<>(driveDto, HttpStatus.OK);
    }
}