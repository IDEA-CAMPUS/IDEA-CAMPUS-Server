package depth.main.ideac.domain.project_post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "프로젝트 목록 조회 Response")
public class ProjectRes {
    private Long id;
    private boolean booleanWeb;
    private boolean booleanApp;
    private boolean booleanAi;
    private String team;
    private String title;
    private String simpleDescription;
    private String thumbnail;
    private Long hits;
    private LocalDateTime createdAt;
}
