package depth.main.ideac.domain.idea_post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class GetAllIdeasRes {
    private Long id;
    private String title;
    @NotBlank(message = "내용이 입력되지 않았습니다.")
    private String simpleDescription;
    private List<String> keyword;
    private String nickName;
    private String color;
    private Long hits;
    private LocalDateTime createdAt;
}
