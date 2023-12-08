package depth.main.ideac.domain.auth.presentation;

import depth.main.ideac.domain.auth.application.AuthService;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth API", description = "Authorization 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/sign-up")
    @Operation(summary = "템플릿 화면", description = "템플릿 화면을 출력합니다.")
    public String signup() {
        System.out.println(" =zzz ");
        return "test";
    }
    @GetMapping(value = "/sign-up")
    @Operation(summary = "템플릿 화면", description = "템플릿 화면을 출력합니다.")
    public String geTsignup() {
        System.out.println(" =get ");
        return "test";
    }
}
