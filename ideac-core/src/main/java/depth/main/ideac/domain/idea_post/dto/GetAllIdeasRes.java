package depth.main.ideac.domain.idea_post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetAllIdeasRes {
    //이미지 추후 추가
//    private Image image
    private String nickName;
    private String title;
    @NotBlank(message = "내용이 입력되지 않았습니다.")
    private String simpleDescription;
    private String keyword;
}