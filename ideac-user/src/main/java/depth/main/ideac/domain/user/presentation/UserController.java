package depth.main.ideac.domain.user.presentation;

import depth.main.ideac.domain.mail.application.MailService;
import depth.main.ideac.domain.user.application.UserService;
import depth.main.ideac.domain.user.dto.PasswordReq;
import depth.main.ideac.global.config.security.token.CurrentUser;
import depth.main.ideac.global.config.security.token.UserPrincipal;
import depth.main.ideac.global.payload.ErrorResponse;
import depth.main.ideac.global.payload.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    @Operation(summary = "비밀번호 바꾸기", description = "비밀번호를 바꾼다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 바꾸기 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "비밀번호 바꾸기 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping(value = "/change-password/{code}")
            public ResponseEntity<?> changePassword(@Parameter(description = "Schemas의 PassWordReq를 참고해주세요.")
                                                        @Valid @RequestBody PasswordReq passwordReq,
                                                    @PathVariable String code) {
        return userService.changePassword(passwordReq,code);
    }

    @Operation(summary = "회원탈퇴", description = "회원탈퇴를 한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원탈퇴 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "회원탈퇴 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        return userService.deleteUser(userPrincipal);
    }

    @Operation(summary = "로그아웃", description = "로그아웃을 한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "로그아웃 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping("/sign-out")
    public ResponseEntity<?> signOut(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        return userService.signOutUser(userPrincipal);
    }
}
