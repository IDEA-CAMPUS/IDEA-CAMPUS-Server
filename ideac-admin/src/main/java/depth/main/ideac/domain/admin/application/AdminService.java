package depth.main.ideac.domain.admin.application;

import depth.main.ideac.domain.admin.dto.UserRes;
import depth.main.ideac.domain.user.domain.Role;
import depth.main.ideac.domain.user.domain.Status;
import depth.main.ideac.domain.user.domain.User;
import depth.main.ideac.domain.user.domain.repository.UserRepository;
import depth.main.ideac.global.error.DefaultException;
import depth.main.ideac.global.payload.ErrorCode;
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
@Transactional(readOnly = true)
public class AdminService {

    private final UserRepository userRepository;

    public boolean isAdmin(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_PARAMETER));

        return user.getRole() == Role.ADMIN || user.getRole() == Role.OWNER;
    }

    public boolean isOwner(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_PARAMETER));

        return user.getRole() == Role.OWNER;
    }

    public Page<UserRes> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.getUsersByStatusNotDelete(pageable);

        List<UserRes> userResList = users.getContent().stream()
                .map(this::convertToUserRes)
                .collect(Collectors.toList());

        return new PageImpl<>(userResList, pageable, users.getTotalElements());
    }

    private UserRes convertToUserRes(User user) {
        return UserRes.builder()
                .id(user.getId())
                .name(user.getName())
                .nickName(user.getNickname())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .createdAt(user.getCreatedAt())
                .status(user.getStatus())
                .role(user.getRole())
                .build();
    }

    public Page<UserRes> searchUser(String word, Pageable pageable) {
        Page<User> users = userRepository.getUsersByStatusNotDeleteAndNameOrNicknameContainingIgnoreCase(word, pageable);

        List<UserRes> userResList = users.getContent().stream()
                .map(this::convertToUserRes)
                .collect(Collectors.toList());

        return new PageImpl<>(userResList, pageable, users.getTotalElements());
    }

    // 상태 변경
    @Transactional
    public UserRes setStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_PARAMETER));

        if (user.getStatus() == Status.ACTIVE) {
            user.updateStatus(Status.SUSPENDED);
        } else if (user.getStatus() == Status.SUSPENDED) {
            user.updateStatus(Status.ACTIVE);
        }
        return convertToUserRes(user);
    }

    // 권한 변경
    @Transactional
    public UserRes setRole(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_PARAMETER));

        if (user.getRole() == Role.USER) {
            user.updateRole(Role.ADMIN);
        } else if (user.getRole() == Role.ADMIN) {
            user.updateRole(Role.USER);
        }
        return convertToUserRes(user);
    }
}
