package depth.main.ideac.domain.club_post.application;

import depth.main.ideac.domain.club_post.ClubPost;
import depth.main.ideac.domain.club_post.dto.ClubPostReq;
import depth.main.ideac.domain.club_post.dto.ClubPostRes;
import depth.main.ideac.domain.club_post.repository.ClubPostRepository;
import depth.main.ideac.domain.club_post.dto.ClubPostDetailRes;
import depth.main.ideac.domain.club_post.dto.UpdateClubPostReq;
import depth.main.ideac.domain.user.domain.Role;
import depth.main.ideac.domain.user.domain.User;
import depth.main.ideac.global.error.DefaultException;
import depth.main.ideac.global.payload.ErrorCode;
import depth.main.ideac.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubPostService {

    private final UserRepository userRepository;
    private final ClubPostRepository clubPostRepository;
    
    // 전체 조회
    public Page<ClubPostRes> getAllClubPosts(Pageable pageable) {
        Page<ClubPost> posts = clubPostRepository.findAllByOrderByCreatedAtDesc(pageable);

        List<ClubPostRes> clubPostResList = posts.getContent().stream()
                .map(clubPost -> ClubPostRes.builder()
                        .title(clubPost.getTitle())
                        .description(clubPost.getDetailedDescription())
                        .createdAt(clubPost.getCreatedAt())
                        .nickname(clubPost.getUser().getNickname())
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(clubPostResList, pageable, posts.getTotalElements());
    }

    // 상세 조회
    public ClubPostDetailRes getDetailClubPosts(Long clubId) {
        ClubPost clubPost = clubPostRepository.findById(clubId)
                .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_PARAMETER));

        return convertToCLubPostDetailRes(clubPost);
    }

    private ClubPostDetailRes convertToCLubPostDetailRes(ClubPost clubPost) {
        return ClubPostDetailRes.builder()
                .title(clubPost.getTitle())
                .description(clubPost.getDetailedDescription())
                .url1(clubPost.getUrl1())
                .url2(clubPost.getUrl2())
                .nickname(clubPost.getUser().getNickname())
                .createdAt(clubPost.getCreatedAt())
                .build();
    }

    @Transactional
    public ClubPostDetailRes createClubPost(Long userId, ClubPostReq clubPostReq) {
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

        return convertToCLubPostDetailRes(clubPost);
    }

    // 글 수정
    @Transactional
    public ClubPostDetailRes updateClubPost(Long clubPostId, UpdateClubPostReq updateClubPostReq) {

        ClubPost clubPost = clubPostRepository.findById(clubPostId)
                .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_PARAMETER));

        clubPost.setTitle(updateClubPostReq.getTitle());
        clubPost.setDetailedDescription(updateClubPostReq.getDescription());
        clubPost.setUrl1(updateClubPostReq.getUrl1());
        clubPost.setUrl2(updateClubPostReq.getUrl2());
        // 이미지 추가 필요

        return convertToCLubPostDetailRes(clubPost);

    }

    // 글 삭제
    @Transactional
    public void deleteClubPost(Long clubPostId) {
        ClubPost clubPost = clubPostRepository.findById(clubPostId)
                .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_PARAMETER));

        clubPostRepository.delete(clubPost);
    }

    // 로그인한 사용자가 관리자인지 작성자 본인인지 확인하는 메소드
    public boolean isAdminOrWriter(Long clubPostId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_PARAMETER));

        ClubPost clubPost = clubPostRepository.findById(clubPostId)
                .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_PARAMETER));

        boolean isAdmin = user.getRole() == Role.ADMIN || user.getRole() == Role.OWNER ;
        boolean isWriter = clubPost.getUser().getId().equals(userId);

        return isAdmin || isWriter;
    }



}
