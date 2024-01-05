package depth.main.ideac.domain.home.application;

import depth.main.ideac.domain.club_post.ClubPost;
import depth.main.ideac.domain.club_post.dto.ClubPostRes;
import depth.main.ideac.domain.club_post.repository.ClubPostRepository;
import depth.main.ideac.domain.idea_post.IdeaPost;
import depth.main.ideac.domain.idea_post.dto.GetAllIdeasRes;
import depth.main.ideac.domain.idea_post.repository.IdeaPostRepository;
import depth.main.ideac.domain.project_post.ProjectPost;
import depth.main.ideac.domain.project_post.dto.response.ProjectRes;
import depth.main.ideac.domain.project_post.repository.ProjectPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeService {

    private final ClubPostRepository clubPostRepository;
    private final IdeaPostRepository ideaPostRepository;
    private final ProjectPostRepository projectPostRepository;

    // 아이디어
    public List<GetAllIdeasRes> getIdeas() {
        List<IdeaPost> ideaPosts = ideaPostRepository.findTop3ByOrderByCreatedAtDesc();

        return ideaPosts.stream()
                .map(this::convertToGetAllIdeasRes)
                .collect(Collectors.toList());
    }

    private GetAllIdeasRes convertToGetAllIdeasRes(IdeaPost ideaPost) {
        return GetAllIdeasRes.builder()
                .title(ideaPost.getTitle())
                .simpleDescription(ideaPost.getSimpleDescription())
                .keyword(ideaPost.getKeyword())
                .nickName(ideaPost.getUser().getNickname())
                .build();
    }

    // 프로젝트
    public List<ProjectRes> getProjects() {
        List<ProjectPost> projectPosts = projectPostRepository.findTop3ByOrderByCreatedAtDesc();

        return projectPosts.stream()
                .map(this::convertToProjectRes)
                .collect(Collectors.toList());
    }

    private ProjectRes convertToProjectRes(ProjectPost projectPost) {
        return ProjectRes.builder()
                .title(projectPost.getTitle())
                .simpleDescription(projectPost.getSimpleDescription())
                .team(projectPost.getTeam())
                .booleanWeb(projectPost.isBooleanWeb())
                .booleanApp(projectPost.isBooleanApp())
                .booleanAi(projectPost.isBooleanAi())
                .build();
    }

    // 동아리
    public List<ClubPostRes> getClubs() {
        List<ClubPost> clubPosts = clubPostRepository.findTop3ByOrderByCreatedAtDesc();

        return clubPosts.stream()
                .map(this::convertToClubPostRes)
                .collect(Collectors.toList());
    }

    private ClubPostRes convertToClubPostRes(ClubPost clubPost) {
        return ClubPostRes.builder()
                .title(clubPost.getTitle())
                .description(clubPost.getDetailedDescription())
                .createdAt(clubPost.getCreatedAt())
                .nickname(clubPost.getUser().getNickname())
                .build();
    }
}
