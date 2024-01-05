package depth.main.ideac.domain.project_post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "프로젝트 게시 Request")
public class PostProjectReq {

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
    private boolean booleanWeb;
    private boolean booleanApp;
    private boolean booleanAi;

//    @OneToMany(mappedBy = "projectPost", cascade = CascadeType.ALL)
//    private List<ProjectPostImage> projectPostImages;

}
