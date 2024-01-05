package depth.main.ideac.domain.banner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BannerRes {

    @Schema( type = "String", example = "배너 제목01" , description="배너 제목")
    private String title;

    @Schema( type = "String", example = "KakaoTalk_20210317_130529791.png" , description="이미지 파일 이름")
    private String originalFileName;

    @Schema( type = "String", example = "https://ideac-github-actions-s3-bucket.s3.amazonaws.com/image/banner/b8d67dc7-265f-4902-9e9f-c3303512ad01.png" , description="이미지 저장 경로")
    private String saveFileUrl;

    private LocalDateTime createdAt;
}
