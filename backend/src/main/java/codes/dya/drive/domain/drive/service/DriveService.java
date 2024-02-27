package codes.dya.drive.domain.drive.service;

import codes.dya.drive.domain.drive.dto.DriveDto;
import codes.dya.drive.domain.drive.entity.Drive;
import codes.dya.drive.domain.drive.repository.DriveRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DriveService {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private static final int PART_SIZE = 10 * 1024 * 1024;  // 10MB
    private final S3Client s3Client;
    private final DriveRepository driveRepository;
    @Value("${cloud.aws.s3.bucket}") private String bucket;

    public void upload(MultipartFile file) throws IOException {

        String fileName = createFileName(file.getOriginalFilename());

        Drive newFile = new Drive(
                file.getOriginalFilename(),
                fileName,
                String.valueOf(getFileSize(file)),
                getFileType(file)
        );

        uploadFile(file, fileName);
        driveRepository.save(newFile);
    }

    public DriveDto.Response find(String id) throws NotFoundException {
        return new DriveDto.Response(driveRepository.findById(id).orElseThrow(NotFoundException::new));
    }

    private void uploadFile(MultipartFile file, String fileName) throws IOException {

        CreateMultipartUploadRequest createMultipartUploadRequest = CreateMultipartUploadRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();
        CreateMultipartUploadResponse createMultipartUploadResponse = s3Client.createMultipartUpload(createMultipartUploadRequest);
        List<CompletedPart> completedParts = new ArrayList<>();

        byte[] data = file.getBytes();
        int parts = (int) Math.ceil((double) data.length / PART_SIZE);

        try {
            for (int i = 0; i < parts; i++) {

                int start = i * PART_SIZE;
                int end = Math.min(start + PART_SIZE, data.length);
                byte[] partData = new byte[end - start];

                System.arraycopy(data, start, partData, 0, partData.length);

                UploadPartRequest uploadPartRequest = UploadPartRequest.builder()
                        .bucket(bucket)
                        .key(fileName)
                        .uploadId(createMultipartUploadResponse.uploadId())
                        .partNumber(i + 1)
                        .build();

                String eTag = s3Client.uploadPart(uploadPartRequest, RequestBody.fromBytes(partData)).eTag();
                completedParts.add(CompletedPart.builder().partNumber(i + 1).eTag(eTag).build());

                int progress = (int) ((double) (i + 1) / parts * 100);
                log.info(fileName + " Uploading: {}%", progress);
            }

            CompletedMultipartUpload completedMultipartUpload = CompletedMultipartUpload.builder()
                    .parts(completedParts)
                    .build();

            CompleteMultipartUploadRequest completeMultipartUploadRequest =
                    CompleteMultipartUploadRequest.builder()
                            .bucket(bucket)
                            .key(fileName)
                            .uploadId(createMultipartUploadResponse.uploadId())
                            .multipartUpload(completedMultipartUpload)
                            .build();

            s3Client.completeMultipartUpload(completeMultipartUploadRequest);

        } catch (Exception e) {
            abortUpload(fileName, createMultipartUploadResponse.uploadId());
            throw new RuntimeException("Failed to upload file to S3: " + e.getMessage());
        }
    }

    private void abortUpload(String key, String uploadId) {
        AbortMultipartUploadRequest abortMultipartUploadRequest = AbortMultipartUploadRequest.builder()
                .bucket(bucket)
                .key(key)
                .uploadId(uploadId)
                .build();

        s3Client.abortMultipartUpload(abortMultipartUploadRequest);
    }

    private String createFileName(String fileName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date()) + "_" + fileName;
    }

    private int getFileSize(MultipartFile file) {
        double bytes = file.getSize();
        return (int) bytes / (1024 * 1024);
    }

    private String getFileType(MultipartFile file) {
        String mimeType = file.getContentType();

        assert mimeType != null;
        if (mimeType.contains("image"))
            return "image";

        if (mimeType.contains("video"))
            return "video";

        if (mimeType.contains("zip") || mimeType.contains("compressed"))
            return "compressed";

        return "unknown";
    }
}