package depth.main.ideac.domain.project_post.dto.response;

import depth.main.ideac.domain.user.domain.User;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProjectDetailRes {
    @NotBlank(message = "제목이 입력되지 않았습니다.")
    private String title;
    @NotBlank(message = "내용이 입력되지 않았습니다.")
    private String simpleDescription;
    @NotBlank(message = "내용이 입력되지 않았습니다.")
    private String detailedDescription;
    private String teamInformation;
    private String githubUrl;
    private String webUrl;
    private String googlePlayUrl;
    private Long hits;
    private boolean booleanWeb;
    private boolean booleanApp;
    private boolean booleanAi;
    private String thumbnail;
    private List<String> otherImages;
}
