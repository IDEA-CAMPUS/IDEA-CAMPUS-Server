package depth.main.ideac.domain.project_post.dto.response;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProjectRes {
    private boolean booleanWeb;
    private boolean booleanApp;
    private boolean booleanAi;
    private String team;
    private String title;
    private String simpleDescription;
    private String thumbnail;
}
