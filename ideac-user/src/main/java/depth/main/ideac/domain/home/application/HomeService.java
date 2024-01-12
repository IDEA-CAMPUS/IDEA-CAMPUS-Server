package depth.main.ideac.domain.home.application;

import depth.main.ideac.domain.club_post.ClubPost;
import depth.main.ideac.domain.club_post.ClubPostImage;
import depth.main.ideac.domain.club_post.dto.response.ClubPostRes;
import depth.main.ideac.domain.club_post.repository.ClubPostRepository;
import depth.main.ideac.domain.idea_post.IdeaPost;
import depth.main.ideac.domain.idea_post.dto.response.GetAllIdeasRes;
import depth.main.ideac.domain.idea_post.repository.IdeaPostRepository;
import depth.main.ideac.domain.project_post.ProjectPost;
import depth.main.ideac.domain.project_post.ProjectPostImage;
import depth.main.ideac.domain.project_post.dto.response.ProjectRes;
import depth.main.ideac.domain.project_post.repository.ProjectPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeService {

    private final ClubPostRepository clubPostRepository;
    private final IdeaPostRepository ideaPostRepository;
    private final ProjectPostRepository projectPostRepository;

    public List<GetAllIdeasRes> getIdeas() {
        List<IdeaPost> ideaPosts = ideaPostRepository.findTop3ByOrderByCreatedAtDesc();

        return ideaPosts.stream()
                .map(ideaPost -> GetAllIdeasRes.builder()
                        .id(ideaPost.getId())
                        .title(ideaPost.getTitle())
                        .simpleDescription(ideaPost.getSimpleDescription())
                        .hits(ideaPost.getHits())
                        .keyword(Arrays.asList(ideaPost.getKeyword().split(",")))
                        .color(ideaPost.getUser().getColor())
                        .nickName(ideaPost.getUser().getNickname())
                        .createdAt(ideaPost.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    // 프로젝트
    public List<ProjectRes> getProjects() {
        List<ProjectPost> projectPosts = projectPostRepository.findTop3ByOrderByCreatedAtDesc();

        return projectPosts.stream()
                .map(projectPost -> {
                    String thumbnail = projectPost.getProjectPostImages().stream()
                            .filter(ProjectPostImage::isThumbnail)
                            .findFirst()
                            .map(ProjectPostImage::getImagePath)
                            .orElse(null);
                    return ProjectRes.builder()
                            .id(projectPost.getId())
                            .booleanWeb(projectPost.isBooleanWeb())
                            .booleanApp(projectPost.isBooleanApp())
                            .booleanAi(projectPost.isBooleanAi())
                            .team(projectPost.getTeam())
                            .title(projectPost.getTitle())
                            .simpleDescription(projectPost.getSimpleDescription())
                            .hits(projectPost.getHits())
                            .createdAt(projectPost.getCreatedAt())
                            .thumbnail(thumbnail)
                            .build();
                })
                .collect(Collectors.toList());
    }

    // 동아리
    public List<ClubPostRes> getClubs() {
        List<ClubPost> clubPosts = clubPostRepository.findTop3ByOrderByCreatedAtDesc();

        return clubPosts.stream()
                .map(clubPost -> ClubPostRes.builder()
                        .id(clubPost.getId())
                        .title(clubPost.getTitle())
                        .description(clubPost.getDetailedDescription())
                        .thumbnail(clubPost.getClubPostImages().stream()
                                .filter(ClubPostImage::isThumbnail)
                                .findFirst()
                                .map(ClubPostImage::getImagePath).orElse(null))
                        .createdAt(clubPost.getCreatedAt())
                        .nickname(clubPost.getUser().getNickname())
                        .build())
                .collect(Collectors.toList());
    }
}
