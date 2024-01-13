package depth.main.ideac.domain.auth.presentation;


import depth.main.ideac.domain.auth.application.AuthService;
import depth.main.ideac.domain.auth.dto.response.AuthRes;
import depth.main.ideac.domain.auth.dto.request.FindIdReq;
import depth.main.ideac.domain.auth.dto.request.SignInReq;
import depth.main.ideac.domain.auth.dto.request.SignUpReq;
import depth.main.ideac.domain.user.dto.PasswordReq;
import depth.main.ideac.global.payload.ErrorResponse;
import depth.main.ideac.global.payload.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth API", description = "Authorization 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입", description = "회원가입을 진행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "회원가입 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping(value = "/sign-up")
    public ResponseEntity<?> signUp(@Parameter(description = "Schemas의 SignUpRequest를 참고해주세요.")
                                        @Valid @RequestBody SignUpReq signUpReq) {
        return authService.signUp(signUpReq);
    }
    @Operation(summary = "로그인", description = "로그인을 진행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AuthRes.class))}),
            @ApiResponse(responseCode = "400", description = "로그인 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping(value = "/sign-in")
    public ResponseEntity<?> signIn(@Parameter(description = "Schemas의 SignInRequest를 참고해주세요.")
                                    @Valid @RequestBody SignInReq signInReq){

        return authService.signIn(signInReq);
    }
    @Operation(summary = "아이디찾기", description = "가입하신 이름과 전화번호로 아이디를 찾는다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이디 찾기 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "아이디 찾기 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping(value = "/find-id")
    public ResponseEntity<?> findId(@Parameter(description = "Schemas의 FindIdRequest를 참고해주세요.")
                                                 @Valid @RequestBody FindIdReq findIdReq){

        return authService.findId(findIdReq);
    }

    @Operation(summary = "닉네임검증", description = "닉네임검증")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "닉네임 검증 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "닉네임 검증 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping(value = "/nickname/{nickname}")
    public ResponseEntity<?> doubleCheckNickname(@PathVariable(value = "nickname") String nickname){

        return authService.doubleCheckNickname(nickname);
    }

    @Operation(summary = "이메일검증", description = "닉네임검증")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 검증 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "이메일 검증 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping(value = "/email/{email}")
    public ResponseEntity<?> doubleCheckEmail(@PathVariable(value = "email") String email){

        return authService.doubleCheckEmail(email);
    }

    @Operation(summary = "비밀번호 바꾸기", description = "비밀번호를 바꾼다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 바꾸기 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "비밀번호 바꾸기 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping(value = "/change-password/{code}")
    public ResponseEntity<?> changePassword(@Parameter(description = "Schemas의 PassWordReq를 참고해주세요.")
                                            @Valid @RequestBody PasswordReq passwordReq,
                                            @PathVariable String code) {
        return authService.changePassword(passwordReq,code);
    }


}
