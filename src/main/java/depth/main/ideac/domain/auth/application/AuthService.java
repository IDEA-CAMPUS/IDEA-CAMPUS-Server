package depth.main.ideac.domain.auth.application;

import depth.main.ideac.domain.auth.domain.Token;
import depth.main.ideac.domain.auth.domain.repository.TokenRepository;
import depth.main.ideac.domain.auth.dto.*;
import depth.main.ideac.domain.user.domain.Provider;
import depth.main.ideac.domain.user.domain.Role;
import depth.main.ideac.domain.user.domain.Status;
import depth.main.ideac.domain.user.domain.User;
import depth.main.ideac.domain.user.domain.repository.UserRepository;
import depth.main.ideac.global.DefaultAssert;
import depth.main.ideac.global.payload.ApiResponse;
import depth.main.ideac.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final CustomTokenProviderService customTokenProviderService;

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    // 회원가입 하기
    public ResponseEntity<?> signUp(SignUpReq signUpRequest){
        //검증
        DefaultAssert.isTrue(!userRepository.existsByEmail(signUpRequest.getIdEmail()), "해당 이메일이 존재합니다.");
        DefaultAssert.isTrue(!userRepository.existsByNickname(signUpRequest.getNickname()), "이미 존재하는 닉네임입니다.");

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
        Optional<User> findUser = userRepository.findByPhoneNumber(findIdReq.getPhoneNumber());
        DefaultAssert.isTrue(findUser.isPresent(), "해당이메일을 갖고 있는 유저가 없습니다.");

        User user = findUser.get();
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(user.getEmail())
                .message("가입하신 아이디를 찾아왔어요!")
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

    public ResponseEntity<?> signout(RefreshTokenReq tokenRefreshRequest){
        boolean checkValid = valid(tokenRefreshRequest.getRefreshToken());
        DefaultAssert.isAuthentication(checkValid);

        //4 token 정보를 삭제한다.
        Optional<Token> token = tokenRepository.findByRefreshToken(tokenRefreshRequest.getRefreshToken());
        tokenRepository.delete(token.get());
        ApiResponse apiResponse = ApiResponse.builder().check(true).information(Message.builder().message("로그아웃 하였습니다.").build()).build();

        return ResponseEntity.ok(apiResponse);
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
}
