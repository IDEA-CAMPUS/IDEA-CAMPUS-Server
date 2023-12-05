package depth.main.ideac.domain.club_post_image.domain;

import depth.main.ideac.domain.club_post.domain.ClubPost;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClubPostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imagePath;

    private boolean isThumbnail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_post_id")
    private ClubPost clubPost;
}
