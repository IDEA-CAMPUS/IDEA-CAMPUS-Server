package depth.main.ideac.global.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {
    @Value("${cloud.aws.s3.bucket}")
    private String BUCKET;

    private final AmazonS3 amazonS3;

    @Transactional
    public String uploadFile(MultipartFile file, String className) throws IOException {
        String fileName = convertToRandomName(file.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        InputStream inputStream = file.getInputStream();

        String filePath = BUCKET + "/image/" + className;
        amazonS3.putObject(new PutObjectRequest(filePath, fileName, inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        String s3ImageKey = "image/" + className + "/" + fileName;
        return s3ImageKey;
    }

    public void deleteFile(String s3ImageKey) {
        amazonS3.deleteObject(new DeleteObjectRequest(BUCKET, s3ImageKey));
    }

    public String convertToRandomName(String originalFileName) {
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return UUID.randomUUID().toString().concat(fileExtension);
    }

    public String getUrl(String s3ImageKey) {
        return amazonS3.getUrl(BUCKET, s3ImageKey).toString();
    }
}
