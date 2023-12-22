package depth.main.ideac.domain.club_post.presentation;

import depth.main.ideac.domain.club_post.application.ClubPostService;
import depth.main.ideac.domain.club_post.dto.ClubPostDetailRes;
import depth.main.ideac.domain.club_post.dto.ClubPostReq;
import depth.main.ideac.domain.club_post.dto.ClubPostRes;
import depth.main.ideac.domain.club_post.dto.UpdateClubPostReq;
import depth.main.ideac.global.config.security.token.CurrentUser;
import depth.main.ideac.global.config.security.token.UserPrincipal;
import depth.main.ideac.global.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/club")
public class ClubPostController {

    private final ClubPostService clubPostService;

    // 글 전체 조회
    @GetMapping
    public ResponseEntity<?> getAllClubPosts(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ClubPostRes> posts = clubPostService.getAllClubPosts(pageable);

        if (posts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(posts)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // 글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getDetailClubPosts(@PathVariable Long id) {
        ClubPostDetailRes clubPostDetailRes = clubPostService.getDetailClubPosts(id);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(clubPostDetailRes)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // 글 등록하기
    @PostMapping("/register")
    public ResponseEntity<?> createClubPost(@CurrentUser UserPrincipal userPrincipal,
                                            @Valid @RequestBody ClubPostReq clubPostReq) {
        ClubPostDetailRes clubPostDetailRes = clubPostService.createClubPost(userPrincipal.getId(), clubPostReq);
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(clubPostDetailRes)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // 글 수정하기
    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateClubPost(@CurrentUser UserPrincipal userPrincipal, @PathVariable Long id,
                                            @Valid @RequestBody UpdateClubPostReq updateClubPostReq) {

        checkPermission(id, userPrincipal.getId());

        ClubPostDetailRes clubPostDetailRes = clubPostService.updateClubPost(id, updateClubPostReq);
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(clubPostDetailRes)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    private void checkPermission(Long clubPostId, Long userId) {
        if (!clubPostService.isAdminOrWriter(clubPostId, userId)) {
            throw new AccessDeniedException("글을 수정할 권한이 없습니다.");
        }
    }

    // 글 삭제하기
    // @PreAuthorize("hasRole('ADMIN') or @clubPostService.isOwner(#clubId, authentication.principal.username)")
    // @PostMapping("/{id}/delete")
}
