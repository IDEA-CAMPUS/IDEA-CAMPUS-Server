package depth.main.ideac.domain.my_page.application;

import depth.main.ideac.domain.my_page.dto.response.UserRes;
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
}
