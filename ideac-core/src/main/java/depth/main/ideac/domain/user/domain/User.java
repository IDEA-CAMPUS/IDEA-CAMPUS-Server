package depth.main.ideac.domain.user.domain;

import depth.main.ideac.domain.auth.dto.SignUpReq;
import depth.main.ideac.domain.club_post.ClubPost;
import depth.main.ideac.domain.common.BaseEntity;
import depth.main.ideac.domain.idea_post.IdeaPost;
import depth.main.ideac.domain.project_post.ProjectPost;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
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

    private String color;

    @OneToMany(mappedBy = "user")
    private List<ProjectPost> projectPosts;

    @OneToMany(mappedBy = "user")
    private List<IdeaPost> ideaPosts;

    @OneToMany(mappedBy = "user")
    private List<ClubPost> clubPosts;


    public void updatePassWord(String pw){
        this.password = pw;
    }
    public void updateStatus(Status status){
        this.status = status;
    }
    public void updateRole(Role role){
        this.role = role;
    }
    public void updateNickName(String nickname) {
        this.nickname = nickname;
    }
    public void googleUpdate(SignUpReq signUpReq){
        this.email = signUpReq.getIdEmail();
        this.name = signUpReq.getName();
        this.nickname = signUpReq.getNickname();
        this.phoneNumber = signUpReq.getPhoneNumber();
        this.organization = signUpReq.getOrganization();
        this.agreeMarketingSms = signUpReq.isAgreeMarketingSms();
    }
}
