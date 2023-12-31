package depth.main.ideac.domain.project_post;

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
public class ProjectPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "제목이 입력되지 않았습니다.")
    private String title;

    @NotBlank(message = "내용이 입력되지 않았습니다.")
    private String simpleDescription;

    @NotBlank(message = "내용이 입력되지 않았습니다.")
    private String detailedDescription;

    private String teamInformation;

    private String githubUrl;

    private String githubUrl1;

    private String webUrl;

    private String googlePlayUrl;

    private boolean isWeb;

    private boolean isApp;

    private boolean isData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "projectPost", cascade = CascadeType.ALL)
    private ProjectPostView projectPostView;

    @OneToMany(mappedBy = "projectPost", cascade = CascadeType.ALL)
    private List<ProjectPostImage> projectPostImages;
}
