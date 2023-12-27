package depth.main.ideac.domain.club_post.dto;

import depth.main.ideac.domain.club_post_image.domain.ClubPostImage;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ClubPostDetailRes {

    private String title;

    private String description;

    private LocalDateTime createdAt;

    private String url1;

    private String url2;

    private String nickname;

    // 이미지 path는 추후 추가
    // private List<ClubPostImage> clubPostImages;
}
