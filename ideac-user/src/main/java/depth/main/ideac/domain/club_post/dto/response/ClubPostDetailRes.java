package depth.main.ideac.domain.club_post.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ClubPostDetailRes {

    private Long id;

    private String title;

    private String description;

    private LocalDateTime createdAt;

    private String url1;

    private String url2;

    private String nickname;

    private String thumbnail;

    private List<String> otherImages;
}
