package codes.dya.drive.util.image;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ImageUploader {

    private static final String PATH = "./images";

    public Image upload(MultipartFile image, String directory) throws IOException {
        String originalFileName = image.getOriginalFilename();
        String imageName = createImageName(originalFileName);
        String imagePath = PATH + "/" + directory + "/" + imageName;
        Path path = Path.of(imagePath).toAbsolutePath();
        File file = path.toFile();

        file.getParentFile().mkdirs();
        image.transferTo(file);
        return new Image(originalFileName, imageName);
    }

    private String createImageName(String fileName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date()) + "_" + fileName;
    }
}
