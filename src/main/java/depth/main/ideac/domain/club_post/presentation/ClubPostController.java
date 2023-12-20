package depth.main.ideac.domain.club_post.presentation;

import depth.main.ideac.domain.club_post.application.ClubPostService;
import depth.main.ideac.domain.club_post.dto.ClubPostReq;
import depth.main.ideac.global.config.security.token.CurrentUser;
import depth.main.ideac.global.config.security.token.UserPrincipal;
import depth.main.ideac.global.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/club")
public class ClubPostController {

    private final ClubPostService clubPostService;

    // 글 전체 조회

    // 글 상세 조회

    // 글 등록하기
    @PostMapping("/register")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createClubPost(@CurrentUser UserPrincipal userPrincipal,
                                            @Valid @RequestBody ClubPostReq clubPostReq) {
        clubPostService.createClubPost(userPrincipal.getId(), clubPostReq);
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information("글이 등록되었습니다.")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // 글 수정하기
    // 글 삭제하기
}
