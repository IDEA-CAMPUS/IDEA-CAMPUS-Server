package depth.main.ideac.domain.idea_post.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class GetDetailIdeaRes {

    private Long id;
    private String color;
    private String nickName;
    private String title;
    private List<String> keyWord;
    private String simpleDescription;
    private String detailedDescription;
    private String url1;
    private String url2;
    private Long hits;
    private LocalDateTime createdAt;
}
