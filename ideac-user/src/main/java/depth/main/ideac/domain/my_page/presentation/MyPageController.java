package depth.main.ideac.domain.my_page.presentation;

import depth.main.ideac.domain.my_page.application.MyPageService;
import depth.main.ideac.domain.my_page.dto.request.UpdateUserReq;
import depth.main.ideac.global.config.security.token.CurrentUser;
import depth.main.ideac.global.config.security.token.UserPrincipal;
import depth.main.ideac.global.payload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "MyPage API", description = "마이페이지 관련 API입니다.")
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

    @Operation(summary = "마이페이지 유저 정보", description = "마이페이지에 표기될 유저 정보를 조회하는 API입니다. 필요 정보를 필터링해 재사용 가능합니다.")
    @GetMapping("/user-info")
    ResponseEntity<?> getMyPageUserInfo(@CurrentUser UserPrincipal userPrincipal){
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(myPageService.getMyPageUserInfo(userPrincipal.getId()))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "회원정보 수정", description = "회원정보 수정 API입니다.")
    @PutMapping("/user-info")
    public ResponseEntity<?> updateUserInfo(@CurrentUser UserPrincipal userPrincipal,
                                            @Valid @RequestBody UpdateUserReq updateUserReq){
        myPageService.updateUserInfo(userPrincipal.getId(), updateUserReq);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "내 게시물 조회", description = "프로젝트, 아이디어, 동아리학회 게시판의 모든 작성글을 최신순으로 조회하는 API입니다.")
    @GetMapping("/posts")
    public ResponseEntity<?> getAllMyPosts(@CurrentUser UserPrincipal userPrincipal){
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(myPageService.getAllMyPosts(userPrincipal.getId()))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}