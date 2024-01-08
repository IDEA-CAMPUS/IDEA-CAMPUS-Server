package depth.main.ideac.domain.club_post.presentation;

import depth.main.ideac.domain.club_post.application.ClubPostService;
import depth.main.ideac.domain.club_post.dto.ClubPostDetailRes;
import depth.main.ideac.domain.club_post.dto.ClubPostReq;
import depth.main.ideac.domain.club_post.dto.ClubPostRes;
import depth.main.ideac.domain.club_post.dto.UpdateClubPostReq;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

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
    // @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'OWNER')")
    @PostMapping
    public ResponseEntity<?> createClubPost(@CurrentUser UserPrincipal userPrincipal,
                                            @Valid @RequestPart ClubPostReq clubPostReq,
                                            @RequestPart("images") List<MultipartFile> images) throws IOException {
        Long clubPostId = clubPostService.createClubPost(userPrincipal.getId(), clubPostReq, images);
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
                                            @Valid @RequestPart UpdateClubPostReq updateClubPostReq,
                                            @RequestPart("images") List<MultipartFile> images) throws IOException {

        checkPermission(id, userPrincipal.getId());

        clubPostService.updateClubPost(id, updateClubPostReq, images);
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information("글이 수정되었습니다.")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // 글 삭제하기
    @Operation(summary = "글 삭제", description = "동아리/학회 글을 삭제하는 API입니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClubPost(@CurrentUser UserPrincipal userPrincipal, @PathVariable Long id) {

        checkPermission(id, userPrincipal.getId());

        clubPostService.deleteClubPost(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information("글이 삭제되었습니다.")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    private void checkPermission(Long clubPostId, Long userId) {
        if (!clubPostService.isAdminOrWriter(clubPostId, userId)) {
            throw new AccessDeniedException("해당 게시글에 대한 권한이 없습니다.");
        }
    }

}
