package depth.main.ideac.domain.club_post.application;

import depth.main.ideac.domain.club_post.domain.ClubPost;
import depth.main.ideac.domain.club_post.domain.repository.ClubPostRepository;
import depth.main.ideac.domain.club_post.dto.ClubPostDetailRes;
import depth.main.ideac.domain.club_post.dto.ClubPostReq;
import depth.main.ideac.domain.club_post.dto.ClubPostRes;
import depth.main.ideac.domain.user.domain.User;
import depth.main.ideac.domain.user.domain.repository.UserRepository;
import depth.main.ideac.global.error.DefaultException;
import depth.main.ideac.global.payload.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClubPostService {

    private final UserRepository userRepository;
    private final ClubPostRepository clubPostRepository;
    
    // 전체 조회
    // public List<ClubPostRes> getIdea()
    // 비회원, 회원 둘 다 가능하게
    // 페이징
    
    // 상세 조회

    public void createClubPost(Long userId, ClubPostReq clubPostReq) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_PARAMETER));

        ClubPost clubPost = ClubPost.builder()
                .title(clubPostReq.getTitle())
                .detailedDescription(clubPostReq.getDescription())
                .url1(clubPostReq.getUrl1())
                .url2(clubPostReq.getUrl2())
                .user(user)
                //.clubPostImages()
                .build();
        clubPostRepository.save(clubPost);
    }
}
