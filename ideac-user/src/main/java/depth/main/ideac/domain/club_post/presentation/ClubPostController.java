package depth.main.ideac.domain.club_post.presentation;

import depth.main.ideac.domain.club_post.application.ClubPostService;
import depth.main.ideac.domain.club_post.dto.request.ClubPostReq;
import depth.main.ideac.domain.club_post.dto.response.ClubPostDetailRes;
import depth.main.ideac.domain.club_post.dto.response.ClubPostRes;
import depth.main.ideac.global.config.security.token.CurrentUser;
import depth.main.ideac.global.config.security.token.UserPrincipal;
import depth.main.ideac.global.payload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@Tag(name = "ClubPost API", description = "동아리/학회 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/club")
public class ClubPostController {

    private final ClubPostService clubPostService;

    // 글 전체 조회
    @Operation(summary = "글 전체 조회", description = "동아리/학회 페이지의 글을 전체 조회하는 API입니다.")
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
    @Operation(summary = "글 상세 조회", description = "동아리/학회 페이지의 글을 상세 조회하는 API입니다.")
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
    @Operation(summary = "글 등록", description = "동아리/학회 글을 등록하는 API입니다.")
    @PostMapping
    public ResponseEntity<?> createClubPost(@CurrentUser UserPrincipal userPrincipal,
                                            @Valid @ModelAttribute ClubPostReq clubPostReq) throws IOException {
        Long clubPostId = clubPostService.createClubPost(userPrincipal.getId(), clubPostReq);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(clubPostId)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    // 글 수정하기
    @Operation(summary = "글 수정", description = "동아리/학회 글을 수정하는 API입니다.")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateClubPost(@CurrentUser UserPrincipal userPrincipal, @PathVariable Long id,
                                            @Valid @ModelAttribute ClubPostReq updateClubPostReq) throws IOException {
        clubPostService.updateClubPost(id, userPrincipal.getId(), updateClubPostReq);
        return ResponseEntity.ok().build();
    }

    // 글 삭제하기
    @Operation(summary = "글 삭제", description = "동아리/학회 글을 삭제하는 API입니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClubPost(@CurrentUser UserPrincipal userPrincipal, @PathVariable Long id) {
        clubPostService.deleteClubPost(id, userPrincipal.getId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "권한 확인", description = "동아리/학회 수정/삭제 권한을 확인하는 API입니다. true: 가능, false: 불가능")
    @GetMapping("/check/{id}")
    private boolean checkPermission(@CurrentUser UserPrincipal userPrincipal,
                                    @PathVariable Long id) {
        return clubPostService.isAdminOrWriter(id, userPrincipal.getId()); // true: 권한있음
    }

}
