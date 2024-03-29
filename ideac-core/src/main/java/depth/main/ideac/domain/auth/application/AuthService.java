package depth.main.ideac.domain.auth.application;

import depth.main.ideac.domain.auth.domain.Token;
import depth.main.ideac.domain.auth.domain.repository.TokenRepository;
import depth.main.ideac.domain.auth.dto.*;
import depth.main.ideac.domain.auth.dto.request.FindIdReq;
import depth.main.ideac.domain.auth.dto.request.RefreshTokenReq;
import depth.main.ideac.domain.auth.dto.request.SignInReq;
import depth.main.ideac.domain.auth.dto.request.SignUpReq;
import depth.main.ideac.domain.auth.dto.response.AuthRes;
import depth.main.ideac.domain.auth.dto.response.GoogleRes;
import depth.main.ideac.domain.mail.domain.Verify;
import depth.main.ideac.domain.mail.domain.repository.MailRepository;
import depth.main.ideac.domain.user.domain.Role;
import depth.main.ideac.domain.user.domain.Status;
import depth.main.ideac.domain.user.domain.User;
import depth.main.ideac.domain.user.domain.repository.UserRepository;
import depth.main.ideac.domain.auth.dto.request.PasswordReq;
import depth.main.ideac.global.DefaultAssert;
import depth.main.ideac.global.error.DefaultException;
import depth.main.ideac.global.payload.ApiResponse;
import depth.main.ideac.global.payload.ErrorCode;
import depth.main.ideac.global.payload.Message;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final CustomTokenProviderService customTokenProviderService;

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final MailRepository mailRepository;

    // 회원가입 하기
    public ResponseEntity<?> signUp(SignUpReq signUpRequest){

        DefaultAssert.isTrue(signUpRequest.getPassword().equals(signUpRequest.getCheckPassword()), "비밀번호가 서로 다릅니다.");

        User user = User.builder()
                        .email(signUpRequest.getIdEmail())
                        .password(passwordEncoder.encode(signUpRequest.getPassword()))
                        .name(signUpRequest.getName())
                        .nickname(signUpRequest.getNickname())
                        .organization(signUpRequest.getOrganization())
                        .phoneNumber(signUpRequest.getPhoneNumber())
                        .agreeMarketingSms(signUpRequest.isAgreeMarketingSms())
                        .role(Role.USER)
                        .status(Status.ACTIVE)
                        .color("#FFCF4A")
                        .build();

        userRepository.save(user);

        ApiResponse apiResponse = ApiResponse.builder().
                check(true)
                .information(Message.builder()
                        .message("회원가입에 성공하였습니다.")
                        .build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    //로그인 하기
    public ResponseEntity<?> signIn(SignInReq signInReq){

        Optional<User> user = userRepository.findByEmail(signInReq.getEmail());
        DefaultAssert.isTrue(user.isPresent(), "이메일이 틀렸습니다.");

        User findUser = user.get();
        if (findUser.getStatus() == Status.SUSPENDED || findUser.getStatus() == Status.DELETE){
            throw new DefaultException(ErrorCode.INVALID_CHECK, "정지되었거나 탈퇴된 유저입니다.");
        }

        boolean checkPassword = passwordEncoder.matches(signInReq.getPassword(), findUser.getPassword());
        DefaultAssert.isTrue(checkPassword, "비밀번호가 틀렸습니다");

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    signInReq.getEmail(),
                    signInReq.getPassword()
            )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenMapping tokenMapping = customTokenProviderService.createToken(authentication);
        Token token = Token.builder()
                            .refreshToken(tokenMapping.getRefreshToken())
                            .userEmail(tokenMapping.getUserEmail())
                            .build();

        tokenRepository.save(token);

        AuthRes authResponse = AuthRes.builder()
                .accessToken(tokenMapping.getAccessToken())
                .refreshToken(token.getRefreshToken()).build();
        return ResponseEntity.ok(authResponse);
    }

    // 핸드폰번호로 아이디(이메일) 찾기
    public ResponseEntity<?> findId(FindIdReq findIdReq) {
        System.out.println("findIdReq.getPhoneNumber() = " + findIdReq.getPhoneNumber());
        Optional<User> findUser = userRepository.findByPhoneNumber(findIdReq.getPhoneNumber());
        DefaultAssert.isTrue(findUser.isPresent(), "해당이메일을 갖고 있는 유저가 없습니다.");
        
        User user = findUser.get();
        System.out.println("user.getEmail() = " + user.getEmail());
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(user.getEmail())
                .build();
        return ResponseEntity.ok(apiResponse);
    }
    public ResponseEntity<?> refresh(RefreshTokenReq tokenRefreshRequest){
        //1차 검증
        boolean checkValid = valid(tokenRefreshRequest.getRefreshToken());
        DefaultAssert.isAuthentication(checkValid);

        Optional<Token> token = tokenRepository.findByRefreshToken(tokenRefreshRequest.getRefreshToken());
        Authentication authentication = customTokenProviderService.getAuthenticationByEmail(token.get().getUserEmail());

        //4. refresh token 정보 값을 업데이트 한다.
        //시간 유효성 확인
        TokenMapping tokenMapping;

        Long expirationTime = customTokenProviderService.getExpiration(tokenRefreshRequest.getRefreshToken());
        if(expirationTime > 0){
            tokenMapping = customTokenProviderService.refreshToken(authentication, token.get().getRefreshToken());
        }else{
            tokenMapping = customTokenProviderService.createToken(authentication);
        }

        Token updateToken = token.get().updateRefreshToken(tokenMapping.getRefreshToken());
        tokenRepository.save(updateToken);

        AuthRes authResponse = AuthRes.builder().accessToken(tokenMapping.getAccessToken()).refreshToken(updateToken.getRefreshToken()).build();

        return ResponseEntity.ok(authResponse);
    }

    private boolean valid(String refreshToken){

        //1. 토큰 형식 물리적 검증
        boolean validateCheck = customTokenProviderService.validateToken(refreshToken);
        DefaultAssert.isTrue(validateCheck, "Token 검증에 실패하였습니다.");

        //2. refresh token 값을 불러온다.
        Optional<Token> token = tokenRepository.findByRefreshToken(refreshToken);
        DefaultAssert.isTrue(token.isPresent(), "탈퇴 처리된 회원입니다.");

        //3. email 값을 통해 인증값을 불러온다
        Authentication authentication = customTokenProviderService.getAuthenticationByEmail(token.get().getUserEmail());
        DefaultAssert.isTrue(token.get().getUserEmail().equals(authentication.getName()), "사용자 인증에 실패하였습니다.");

        return true;
    }

    // 닉네임 중복검증
    public ResponseEntity<?> doubleCheckNickname(String nickname) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(userRepository.findByNickname(nickname).isEmpty())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // 이메일 중복검증
    public ResponseEntity<?> doubleCheckEmail(String email) {
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(userRepository.findByEmail(email).isEmpty())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<?> changePassword(@Valid PasswordReq passwordReq, String code){

        //검증
        DefaultAssert.isTrue(passwordReq.getPassword().equals(passwordReq.getRePassword()), "비밀번호가 서로 다릅니다.");
        //만료시간 검증
        Verify verify = mailRepository.findByCode(code);

        if (verify == null){
            throw new DefaultException(ErrorCode.INVALID_CHECK, "이미변경되었습니다.");
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
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> getGoogleInfoByToken(String token) {
        Long userId = customTokenProviderService.getUserIdFromToken(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DefaultException(ErrorCode.USER_NOT_FOUND));
        GoogleRes googleRes = GoogleRes.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(googleRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
