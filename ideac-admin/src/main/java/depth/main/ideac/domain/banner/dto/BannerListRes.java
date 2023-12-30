package depth.main.ideac.domain.banner.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BannerListRes {

    private Long id;

    private String title;

    private int number; // 정렬 순서에 따른 배너 번호

    private LocalDateTime createdAt;

    private String nickName;
}
