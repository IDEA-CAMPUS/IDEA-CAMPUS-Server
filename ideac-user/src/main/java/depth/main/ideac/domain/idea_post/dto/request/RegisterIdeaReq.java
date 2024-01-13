package depth.main.ideac.domain.idea_post.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class RegisterIdeaReq {
    @NotBlank(message = "제목이 입력되지 않았습니다.")
    @Size(max = 15, message = "제목은 최대 15자까지 입력 가능합니다.")
    private String title;
    @NotBlank(message = "내용이 입력되지 않았습니다.")
    @Size(max = 50, message = "간단 설명은 최대 50자까지 입력 가능합니다.")
    private String simpleDescription;
    @NotBlank
    private String keyword;
    @NotBlank(message = "내용이 입력되지 않았습니다.")
    private String detailedDescription;
    private String url1;
    private String url2;
}
