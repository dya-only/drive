package codes.dya.drive.domain.drive.dto;

import codes.dya.drive.domain.drive.entity.Drive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class DriveDto {

//    @Getter
//    @Setter
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class Request {
//        String originalName;
//        String fileName;
//        String size;
//        String type;
//    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Response {
        String id;
        String originalName;
        String fileName;
        String size;
        String type;

        public Response(Drive drive) {
            this.id = drive.getId();
            this.originalName = drive.getOriginalName();
            this.fileName = drive.getFileName();
            this.size = drive.getSize();
            this.type = drive.getType();
        }
    }
}
