package depth.main.ideac.domain.banner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BannerListRes {

    @Schema( type = "Long", example = "1" , description="배너 id")
    private Long id;

    @Schema( type = "String", example = "배너 제목01" , description="배너 제목")
    private String title;

    @Schema( type = "String", example = "1" , description="배너 조회 결과 순번")
    private int number; // 정렬 순서에 따른 배너 번호

    private LocalDateTime createdAt;

    private String nickName;
}
