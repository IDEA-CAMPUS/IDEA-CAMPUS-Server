package depth.main.ideac.domain.auth.presentation;


import depth.main.ideac.domain.auth.application.AuthService;
import depth.main.ideac.domain.auth.dto.FindIdReq;
import depth.main.ideac.domain.auth.dto.SignUpReq;
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
    public ResponseEntity<?> signup(@Parameter(description = "Schemas의 SignUpRequest를 참고해주세요.")
                                        @Valid @RequestBody SignUpReq signUpReq) {
        System.out.println("접근");
        return authService.signup(signUpReq);
    }
    @Operation(summary = "아이디찾기", description = "가입하신 이름과 전화번호로 아이디를 찾는다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이디 찾기 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "아이디 찾기 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping(value = "/findId")
    public ResponseEntity<?> findId(@Parameter(description = "Schemas의 SignUpRequest를 참고해주세요.")
                                                 @Valid @RequestBody FindIdReq findIdReq){

        return authService.findId(findIdReq);
    }
}
