package depth.main.ideac.domain.idea_post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateIdeaReq {
    @NotBlank(message = "제목이 입력되지 않았습니다.")
    private String title;
    private String keyWord;
    @NotBlank(message = "내용이 입력되지 않았습니다.")
    private String simpleDescription;
    @NotBlank(message = "내용이 입력되지 않았습니다.")
    private String detailedDescription;
    private String url1;
    private String url2;
}
