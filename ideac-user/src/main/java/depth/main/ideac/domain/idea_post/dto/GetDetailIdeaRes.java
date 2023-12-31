package depth.main.ideac.domain.idea_post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetDetailIdeaRes {

    private String color;
    private String nickName;
    private String title;
    private String keyWord;
    private String simpleDescription;
    private String detailedDescription;
    private String url1;
    private String url2;
    private Long hits;
}
