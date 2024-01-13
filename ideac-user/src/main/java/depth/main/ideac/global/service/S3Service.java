package depth.main.ideac.global.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import depth.main.ideac.global.DefaultAssert;
import depth.main.ideac.global.error.DefaultNullPointerException;
import depth.main.ideac.global.payload.ErrorCode;
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
public class S3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String BUCKET;

    private final AmazonS3 amazonS3;

    @Transactional
    public String uploadFile(MultipartFile file, String className) throws IOException {
        if(file == null){
            throw new DefaultNullPointerException(ErrorCode.INVALID_PARAMETER, "파일을 첨부해주세요..");
        }
        String fileName = convertToRandomName(file.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        InputStream inputStream = file.getInputStream();

        String filePath = BUCKET + "/image/" + className;
        amazonS3.putObject(new PutObjectRequest(filePath, fileName, inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        String s3key = "image/" + className + "/" + fileName;
        return s3key;
    }
    @Transactional
    public void deleteFile(String s3key) {
        if(s3key == null){
            throw new DefaultNullPointerException(ErrorCode.INVALID_PARAMETER, "삭제하려는 파일을 찾을 수 없습니다.");
        }
        amazonS3.deleteObject(new DeleteObjectRequest(BUCKET, s3key));
    }

    public String convertToRandomName(String originalFileName) {
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return UUID.randomUUID().toString().concat(fileExtension);
    }

    public String getUrl(String s3key) {
        if(s3key == null){
            throw new DefaultNullPointerException(ErrorCode.INVALID_PARAMETER, "파일을 찾을 수 없습니다.");
        }
        return amazonS3.getUrl(BUCKET, s3key).toString();
    }
}
