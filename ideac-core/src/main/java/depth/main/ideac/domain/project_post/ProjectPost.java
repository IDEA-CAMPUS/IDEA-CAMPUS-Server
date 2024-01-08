package depth.main.ideac.domain.project_post;

import depth.main.ideac.domain.common.BaseEntity;
import depth.main.ideac.domain.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "제목이 입력되지 않았습니다.")
    @Size(max = 15, message = "제목은 최대 15자까지 입력 가능합니다.")
    private String title;

    @NotBlank(message = "내용이 입력되지 않았습니다.")
    @Size(max = 50, message = "간단 설명은 최대 50자까지 입력 가능합니다.")
    private String simpleDescription;

    @NotBlank(message = "내용이 입력되지 않았습니다.")
    private String detailedDescription;

    private String teamInformation;

    private String githubUrl;

    private String webUrl;

    private String googlePlayUrl;

    private boolean booleanWeb;

    private boolean booleanApp;

    private boolean booleanAi;

    private String team;

    private Long hits;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "projectPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectPostImage> projectPostImages;

}
