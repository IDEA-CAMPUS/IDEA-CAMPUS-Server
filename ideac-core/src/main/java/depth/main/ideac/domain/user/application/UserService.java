package depth.main.ideac.domain.user.application;

import depth.main.ideac.domain.club_post.ClubPost;
import depth.main.ideac.domain.mail.domain.Verify;
import depth.main.ideac.domain.mail.domain.repository.MailRepository;
import depth.main.ideac.domain.user.domain.Role;
import depth.main.ideac.domain.user.domain.User;
import depth.main.ideac.domain.user.domain.repository.UserRepository;
import depth.main.ideac.global.DefaultAssert;
import depth.main.ideac.global.error.DefaultException;
import depth.main.ideac.global.payload.ApiResponse;
import depth.main.ideac.global.payload.ErrorCode;
import depth.main.ideac.domain.user.dto.PasswordReq;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final MailRepository mailRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Transactional
    public ResponseEntity<?> changePassword(@Valid PasswordReq passwordReq, String code){

        //검증
        DefaultAssert.isTrue(passwordReq.getPassword().equals(passwordReq.getRePassword()), "비밀번호가 서로 다릅니다.");
        //만료시간 검증
        Verify verify = mailRepository.findByCode(code);

        if (verify == null){
            throw new DefaultException(ErrorCode.INVALID_CHECK, "이미변경되었습니다?");
        }

        DefaultAssert.isTrue(verify.checkExpiration(LocalDateTime.now()), "만료되었습니다.");

        Optional<User> findUser = userRepository.findByEmail(verify.getEmail());

        User user = findUser.get();
        user.updatePassWord(passwordEncoder.encode(passwordReq.getPassword()));

        // 인증완료 후 삭제
        mailRepository.delete(verify);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(null)
                .message("비밀번호를 바꾸었어요")
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
