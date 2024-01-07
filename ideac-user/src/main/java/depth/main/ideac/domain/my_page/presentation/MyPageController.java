package depth.main.ideac.domain.my_page.presentation;

import depth.main.ideac.domain.my_page.application.MyPageService;
import depth.main.ideac.global.config.security.token.CurrentUser;
import depth.main.ideac.global.config.security.token.UserPrincipal;
import depth.main.ideac.global.payload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/my-page")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @Operation(summary = "헤더 표기정보", description = "헤더에 표기될 프로필 색상 및 닉네임을 조회하는 API입니다.")
    @GetMapping("/header/user-info")
    public ResponseEntity<?> getHeaderUserInfo(@CurrentUser UserPrincipal userPrincipal){
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(myPageService.getHeaderUserInfo(userPrincipal.getId()))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}