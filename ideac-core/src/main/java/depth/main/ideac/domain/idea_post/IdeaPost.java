package depth.main.ideac.domain.idea_post;

import depth.main.ideac.domain.common.BaseEntity;
import depth.main.ideac.domain.idea_post.dto.UpdateIdeaReq;
import depth.main.ideac.domain.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
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

//    public void updateIdea(UpdateIdeaReq updateIdeaReq) {
//        this.title = updateIdeaReq.getTitle();
//        this.simpleDescription = updateIdeaReq.getSimpleDescription();
//        this.detailedDescription = updateIdeaReq.getDetailedDescription();
//        this.url1 = updateIdeaReq.getUrl1();
//        this.url2 = updateIdeaReq.getUrl2();
//    }
}
