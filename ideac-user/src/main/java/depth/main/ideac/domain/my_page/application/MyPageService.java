package depth.main.ideac.domain.my_page.application;

import depth.main.ideac.domain.club_post.ClubPost;
import depth.main.ideac.domain.club_post.repository.ClubPostRepository;
import depth.main.ideac.domain.idea_post.IdeaPost;
import depth.main.ideac.domain.idea_post.repository.IdeaPostRepository;
import depth.main.ideac.domain.my_page.dto.request.UpdateUserReq;
import depth.main.ideac.domain.my_page.dto.response.PostRes;
import depth.main.ideac.domain.my_page.dto.response.UserRes;
import depth.main.ideac.domain.project_post.ProjectPost;
import depth.main.ideac.domain.project_post.repository.ProjectPostRepository;
import depth.main.ideac.domain.user.domain.User;
import depth.main.ideac.domain.user.domain.repository.UserRepository;
import depth.main.ideac.global.error.DefaultException;
import depth.main.ideac.global.payload.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MyPageService {
    private final UserRepository userRepository;
    private final ProjectPostRepository projectPostRepository;
    private final ClubPostRepository clubPostRepository;
    private final IdeaPostRepository ideaPostRepository;

    public UserRes.HeaderInfoRes getHeaderUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DefaultException(ErrorCode.USER_NOT_FOUND));
        return UserRes.HeaderInfoRes.builder()
                .nickname(user.getNickname())
                .color(user.getColor())
                .build();
    }
    public UserRes.MyPageInfoRes getMyPageUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DefaultException(ErrorCode.USER_NOT_FOUND));
        return UserRes.MyPageInfoRes.builder()
                .color(user.getColor())
                .name(user.getName())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .organization(user.getOrganization())
                .build();
    }
    @Transactional
    public void updateUserInfo(Long userId, UpdateUserReq updateUserReq) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DefaultException(ErrorCode.USER_NOT_FOUND));
        user.setName(updateUserReq.getName());
        user.setNickname(updateUserReq.getNickname());
        user.setEmail(updateUserReq.getEmail());
        user.setPhoneNumber(updateUserReq.getPhoneNumber());
        user.setOrganization(updateUserReq.getOrganization());
        user.setColor(updateUserReq.getColor());
        userRepository.save(user);
    }
    public List<PostRes> getAllMyPosts(Long userId) {
        List<PostRes> postResList = new ArrayList<>();
        List<ProjectPost> projectPosts = projectPostRepository.findAllByUserId(userId);
        List<ClubPost> clubPosts = clubPostRepository.findAllByUserId(userId);
        List<IdeaPost> ideaPosts = ideaPostRepository.findAllByUserId(userId);

        postResList.addAll(projectPosts.stream()
                .map(post -> PostRes.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .type(post.getClass().getSimpleName())
                        .createdAt(post.getCreatedAt())
                        .build())
                .toList());
        postResList.addAll(clubPosts.stream()
                .map(post -> PostRes.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .type(post.getClass().getSimpleName())
                        .createdAt(post.getCreatedAt())
                        .build())
                .toList());
        postResList.addAll(ideaPosts.stream()
                .map(post -> PostRes.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .type(post.getClass().getSimpleName())
                        .createdAt(post.getCreatedAt())
                        .build())
                .toList());
        postResList.sort(Comparator.comparing(PostRes::getCreatedAt).reversed());
        return postResList;
    }
}
