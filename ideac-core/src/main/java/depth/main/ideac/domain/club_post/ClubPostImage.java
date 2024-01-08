package depth.main.ideac.domain.club_post;

import depth.main.ideac.domain.club_post.ClubPost;
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

    private String s3key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_post_id")
    private ClubPost clubPost;
}
