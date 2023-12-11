package depth.main.ideac.domain.user.domain;

import depth.main.ideac.domain.banner.domain.Banner;
import depth.main.ideac.domain.club_post.domain.ClubPost;
import depth.main.ideac.domain.common.BaseEntity;
import depth.main.ideac.domain.idea_post.domain.IdeaPost;
import depth.main.ideac.domain.project_post.domain.ProjectPost;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "이메일 형식이어야 합니다.")
    // @Pattern
    @NotBlank(message = "이메일이 입력되지 않았습니다.")
    private String email;

    @Size(min = 8, max = 16)
    private String password;

    @Min(value = 2, message = "2자 이상 입력해주세요")
    private String name;

    @Column(unique = true)
    private String nickname;

    private String organization;

    @Pattern(regexp = "^01(?:0|1|[6-9])(\\\\d{3}|\\\\d{4})(\\\\d{4})$", message = "휴대폰번호를 정확하게 입력해주세요")
    private String phoneNumber;

    private boolean agreeMarketingSms;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    private String providerId;

    private Role role = Role.USER;

    private Status status = Status.ACTIVE;

    private boolean isSocial;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<ProjectPost> projectPosts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<IdeaPost> ideaPosts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<ClubPost> clubPosts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Banner> banners;

    public void updateName(String name){
        this.name = name;
    }
}
