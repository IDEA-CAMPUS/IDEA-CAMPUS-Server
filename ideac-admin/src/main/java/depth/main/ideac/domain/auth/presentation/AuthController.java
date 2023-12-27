package depth.main.ideac.domain.auth.presentation;


import depth.main.ideac.domain.auth.application.AuthService;
import depth.main.ideac.domain.auth.dto.AuthRes;
import depth.main.ideac.domain.auth.dto.FindIdReq;
import depth.main.ideac.domain.auth.dto.SignInReq;
import depth.main.ideac.domain.auth.dto.SignUpReq;
import depth.main.ideac.domain.user.application.UserService;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth API", description = "Authorization 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인", description = "로그인을 진행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AuthRes.class))}),
            @ApiResponse(responseCode = "400", description = "로그인 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping(value = "/sign-in")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    public ResponseEntity<?> signIn(@Parameter(description = "Schemas의 SignInRequest를 참고해주세요.")
                                    @Valid @RequestBody SignInReq signInReq){


        return authService.signIn(signInReq);
    }

    @Operation(summary = "아이디찾기", description = "가입한 이름과 전화번호로 아이디를 찾는다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이디 찾기 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "아이디 찾기 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping(value = "/find-id")
    public ResponseEntity<?> findId(@Parameter(description = "Schemas의 FindIdRequest를 참고해주세요.")
                                                 @Valid @RequestBody FindIdReq findIdReq){

        return authService.findId(findIdReq);
    }

}
