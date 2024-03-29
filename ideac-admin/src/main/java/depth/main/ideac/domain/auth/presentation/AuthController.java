package depth.main.ideac.domain.auth.presentation;


import depth.main.ideac.domain.admin.application.AdminService;
import depth.main.ideac.domain.auth.application.AuthService;
import depth.main.ideac.domain.auth.dto.response.AuthRes;
import depth.main.ideac.domain.auth.dto.request.FindIdReq;
import depth.main.ideac.domain.auth.dto.request.SignInReq;
import depth.main.ideac.domain.auth.dto.request.PasswordReq;
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
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth API", description = "Authorization 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AdminService adminService;

    @Operation(summary = "로그인", description = "로그인 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AuthRes.class))}),
            @ApiResponse(responseCode = "400", description = "로그인 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping(value = "/sign-in")
    // @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    public ResponseEntity<?> signIn(@Parameter(description = "Schemas의 SignInRequest를 참고해주세요.")
                                    @Valid @RequestBody SignInReq signInReq){

        checkPermission(signInReq.getEmail());
        return authService.signIn(signInReq);
    }

    private void checkPermission(String email) {
        if (!adminService.isAdmin(email)) {
            throw new AccessDeniedException("해당 페이지에 대한 권한이 없습니다.");
        }
    }

    @Operation(summary = "아이디 찾기", description = "가입한 이름과 전화번호로 아이디를 찾는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이디 찾기 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "아이디 찾기 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping(value = "/find-id")
    public ResponseEntity<?> findId(@Parameter(description = "Schemas의 FindIdRequest를 참고해주세요.")
                                                 @Valid @RequestBody FindIdReq findIdReq){

        return authService.findId(findIdReq);
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
