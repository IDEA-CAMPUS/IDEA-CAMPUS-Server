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

    private String password;

    @Size(min = 2, message = "2자 이상 입력해주세요")
    private String name;

    @Column(unique = true)
    private String nickname;

    private String organization;

    @Pattern(regexp = "^01([0|1|6|7|8|9])?([0-9]{3,4})?([0-9]{4})$", message = "번호를 정확하게 입력해주세요")
    private String phoneNumber;

    private boolean agreeMarketingSms;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    private String providerId;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    private boolean isSocial;

    @OneToMany(mappedBy = "user")
    private List<ProjectPost> projectPosts;

    @OneToMany(mappedBy = "user")
    private List<IdeaPost> ideaPosts;

    @OneToMany(mappedBy = "user")
    private List<ClubPost> clubPosts;

    public void updatePassWord(String pw){
        this.password = pw;
    }

}
