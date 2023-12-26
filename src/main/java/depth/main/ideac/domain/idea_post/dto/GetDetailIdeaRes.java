package depth.main.ideac.domain.idea_post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetDetailIdeaRes {
    private String title;
    private String simpleDescription;
    private String detailedDescription;
    private String url1;
    private String url2;
}
