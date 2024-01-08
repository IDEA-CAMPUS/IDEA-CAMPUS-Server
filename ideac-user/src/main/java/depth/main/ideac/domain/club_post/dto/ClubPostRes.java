package depth.main.ideac.domain.club_post.dto;

import depth.main.ideac.domain.club_post.ClubPost;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ClubPostRes {

    private String title;

    private String description;

    private LocalDateTime createdAt;

    private String nickname;

     private String thumbnail;

}
