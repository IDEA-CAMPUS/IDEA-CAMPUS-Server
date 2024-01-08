package depth.main.ideac.domain.project_post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "프로젝트 키워드 Request")
public class ProjectKeywordReq {
    private boolean booleanWeb;
    private boolean booleanApp;
    private boolean booleanAi;
}
