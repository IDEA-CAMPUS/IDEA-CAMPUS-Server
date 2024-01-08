package depth.main.ideac.domain.idea_post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GetAllIdeasRes {
    private String title;
    @NotBlank(message = "내용이 입력되지 않았습니다.")
    private String simpleDescription;
    private String keyword;
    private String nickName;
    private String color;
}
