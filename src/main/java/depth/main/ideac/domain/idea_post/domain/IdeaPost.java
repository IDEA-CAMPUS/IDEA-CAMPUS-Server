package depth.main.ideac.domain.idea_post.domain;

import depth.main.ideac.domain.idea_post_view.domain.IdeaPostView;
import depth.main.ideac.domain.user.domain.User;
import depth.main.ideac.domain.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IdeaPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "제목이 입력되지 않았습니다.")
    private String title;

    private String keyword;

    @NotBlank(message = "내용이 입력되지 않았습니다.")
    private String simpleDescription;

    @NotBlank(message = "내용이 입력되지 않았습니다.")
    private String detailedDescription;

    private String url1;

    private String url2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "ideaPost", cascade = CascadeType.ALL)
    private IdeaPostView ideaPostView;

}
