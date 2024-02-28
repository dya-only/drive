package codes.dya.drive.domain.image.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("api/image")
public class ImageController {

    private static final String PATH = "./images";

    @GetMapping("profile/{fileName}")
    public ResponseEntity<Resource> getProfileImage(
            @PathVariable("fileName") String fileName) throws Exception {

        String str = URLEncoder.encode(fileName, StandardCharsets.UTF_8);

        Path path = Paths.get(PATH + "/profile/" + fileName);
        Resource resource = new InputStreamResource(Files.newInputStream(path));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/octect-stream")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + str + ";")
                .body(resource);
    }
}
