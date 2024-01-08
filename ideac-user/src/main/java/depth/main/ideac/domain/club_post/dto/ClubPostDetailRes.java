package depth.main.ideac.domain.club_post.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ClubPostDetailRes {

    private String title;

    private String description;

    private LocalDateTime createdAt;

    private String url1;

    private String url2;

    private String nickname;

    private String thumbnail;

    private List<String> otherImages;
}
