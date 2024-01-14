package depth.main.ideac.domain.user.application;

import depth.main.ideac.domain.auth.domain.Token;
import depth.main.ideac.domain.auth.domain.repository.TokenRepository;
import depth.main.ideac.domain.auth.dto.request.GoogleSignUpReq;
import depth.main.ideac.domain.auth.dto.request.SignUpReq;
import depth.main.ideac.domain.mail.domain.repository.MailRepository;
import depth.main.ideac.domain.user.domain.Status;
import depth.main.ideac.domain.user.domain.User;
import depth.main.ideac.domain.user.domain.repository.UserRepository;
import depth.main.ideac.global.DefaultAssert;
import depth.main.ideac.global.config.security.token.UserPrincipal;
import depth.main.ideac.global.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final MailRepository mailRepository;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @Transactional
    public ResponseEntity<?> deleteUser(UserPrincipal userPrincipal) {

        User user = getUser(userPrincipal);

        Optional<Token> byUserEmail = tokenRepository.findByUserEmail(userPrincipal.getEmail());
        Token userToken = byUserEmail.get();

        //유저삭제
        user.updateStatus(Status.DELETE);

        //논리삭제를 하기때문에 닉네임을 업데이트한다.

        //유저닉네임변경(추후 같은 아이디로 재 가입시 닉네임 중복을 방지하게 위헤)
        user.updateNickName("deletedUser"+user.getNickname());

        //유저가 가지고 있는 토큰삭제
        tokenRepository.delete(userToken);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(null)
                .message("회원탈퇴 완료하였습니다.")
                .build();

        return ResponseEntity.ok(apiResponse);
    }
    private User getUser(UserPrincipal userPrincipal){
        Optional<User> findUser = userRepository.findByEmail(userPrincipal.getEmail());
        return findUser.get();
    }

    @Transactional
    public ResponseEntity<?> signOutUser(UserPrincipal userPrincipal) {

        Optional<Token> byUserEmail = tokenRepository.findByUserEmail(userPrincipal.getEmail());
        DefaultAssert.isTrue(byUserEmail.isPresent(), "이미 로그인 되어있습니다.");

        //토큰삭제로 로그아웃처리
        Token token = byUserEmail.get();
        tokenRepository.delete(token);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(null)
                .message("로그아웃 완료!")
                .build();

        return ResponseEntity.ok(apiResponse);
    }
    @Transactional
    public ResponseEntity<?> googleUpdate(UserPrincipal userPrincipal, GoogleSignUpReq signUpReq) {
        DefaultAssert.isTrue(!userRepository.existsByNickname(signUpReq.getNickname()), "이미 존재하는 닉네임입니다.");

        User user = userRepository.findById(userPrincipal.getId()).get();
        user.googleUpdate(signUpReq);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(null)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
