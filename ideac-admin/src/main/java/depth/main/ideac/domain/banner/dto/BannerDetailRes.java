package depth.main.ideac.domain.banner.dto;

import depth.main.ideac.domain.banner.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BannerDetailRes {

    @Schema( type = "Long", example = "1" , description="배너 id")
    private Long id;

    @Schema( type = "String", example = "배너 제목01" , description="배너 제목")
    private String title;

    @Schema( type = "String", example = "KakaoTalk_20210317_130529791.png" , description="이미지 파일 이름")
    private String originalFileName;

    @Schema( type = "String", example = "https://ideac-github-actions-s3-bucket.s3.amazonaws.com/image/banner/b8d67dc7-265f-4902-9e9f-c3303512ad01.png" , description="이미지 저장 경로")
    private String saveFileUrl;

    @Schema( type = "String", example = "HOME" , description="배너 타입")
    private Type type;

    private LocalDateTime createdAt;

    @Schema( type = "String", example = "1" , description="배너를 등록한 사용자의 닉네임")
    private String nickName;
}
