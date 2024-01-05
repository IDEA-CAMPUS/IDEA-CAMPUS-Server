package depth.main.ideac.domain.project_post.application;

import depth.main.ideac.domain.project_post.ProjectPost;
import depth.main.ideac.domain.project_post.dto.request.PostProjectReq;
import depth.main.ideac.domain.project_post.repository.ProjectPostRepository;
import depth.main.ideac.domain.user.domain.User;
import depth.main.ideac.domain.user.domain.repository.UserRepository;
import depth.main.ideac.global.error.DefaultException;
import depth.main.ideac.global.payload.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectPostService {

    private final ProjectPostRepository projectPostRepository;
    private final UserRepository userRepository;

    @Transactional
    public void postProject(Long userId, PostProjectReq postProjectReq) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DefaultException(ErrorCode.USER_NOT_FOUND));
        if (!postProjectReq.isBooleanWeb() && !postProjectReq.isBooleanApp() && !postProjectReq.isBooleanAi()) {
                throw new DefaultException(ErrorCode.INVALID_PARAMETER, "키워드는 하나 이상 표시해야 합니다.");
        }
        ProjectPost projectPost = ProjectPost.builder()
                .title(postProjectReq.getTitle())
                .simpleDescription(postProjectReq.getSimpleDescription())
                .detailedDescription(postProjectReq.getDetailedDescription())
                .teamInformation(postProjectReq.getTeamInformation())
                .githubUrl(postProjectReq.getGithubUrl())
                .webUrl(postProjectReq.getWebUrl())
                .googlePlayUrl(postProjectReq.getGooglePlayUrl())
                .booleanWeb(postProjectReq.isBooleanWeb())
                .booleanApp(postProjectReq.isBooleanApp())
                .booleanAi(postProjectReq.isBooleanAi())
                .team(user.getOrganization())
                .user(user)
//                .projectPostView(null)
//                .projectPostImages(postProjectReq.getProjectPostImages)
                .build();
        projectPostRepository.save(projectPost);
    }
}
