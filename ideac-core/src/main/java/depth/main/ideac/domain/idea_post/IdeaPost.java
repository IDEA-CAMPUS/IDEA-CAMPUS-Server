package depth.main.ideac.domain.idea_post;

import depth.main.ideac.domain.common.BaseEntity;
import depth.main.ideac.domain.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(max = 15, message = "제목은 최대 15자까지 입력 가능합니다.")
    private String title;

    private String keyword;

    @NotBlank(message = "내용이 입력되지 않았습니다.")
    @Size(max = 50, message = "간단 설명은 최대 50자까지 입력 가능합니다.")
    private String simpleDescription;

    @NotBlank(message = "내용이 입력되지 않았습니다.")
    private String detailedDescription;

    private String url1;

    private String url2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Long hits;
}
