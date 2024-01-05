package depth.main.ideac.domain.home.application;

import depth.main.ideac.domain.club_post.ClubPost;
import depth.main.ideac.domain.club_post.dto.ClubPostRes;
import depth.main.ideac.domain.club_post.repository.ClubPostRepository;
import depth.main.ideac.domain.idea_post.IdeaPost;
import depth.main.ideac.domain.idea_post.dto.GetAllIdeasRes;
import depth.main.ideac.domain.idea_post.repository.IdeaPostRepository;
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
