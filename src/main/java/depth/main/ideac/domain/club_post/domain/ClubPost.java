package depth.main.ideac.domain.club_post.domain;

import depth.main.ideac.domain.club_post_image.domain.ClubPostImage;
import depth.main.ideac.domain.common.BaseEntity;
import depth.main.ideac.domain.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClubPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "제목이 입력되지 않았습니다.")
    private String title;

    @NotBlank(message = "내용이 입력되지 않았습니다.")
    private String detailedDescription;

    private String url1;

    private String url2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "clubPost", cascade = CascadeType.REMOVE)
    private List<ClubPostImage> clubPostImages;

}
