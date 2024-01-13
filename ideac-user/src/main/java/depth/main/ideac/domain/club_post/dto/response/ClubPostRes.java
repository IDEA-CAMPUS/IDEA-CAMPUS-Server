package depth.main.ideac.domain.club_post.dto.response;

import lombok.Builder;
import lombok.Getter;


import java.time.LocalDateTime;

@Getter
@Builder
public class ClubPostRes {

    private Long id;

    private String title;

    private String description;

    private LocalDateTime createdAt;

    private String nickname;

     private String thumbnail;

}
