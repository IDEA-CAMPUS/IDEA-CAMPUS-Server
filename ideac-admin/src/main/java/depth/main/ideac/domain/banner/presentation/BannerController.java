package depth.main.ideac.domain.banner.presentation;

import depth.main.ideac.domain.admin.application.AdminService;
import depth.main.ideac.domain.banner.Type;
import depth.main.ideac.domain.banner.application.BannerService;
import depth.main.ideac.domain.banner.dto.BannerDetailRes;
import depth.main.ideac.domain.banner.dto.BannerListRes;
import depth.main.ideac.global.config.security.token.CurrentUser;
import depth.main.ideac.global.config.security.token.UserPrincipal;
import depth.main.ideac.global.payload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "Banner API", description = "관리자 페이지 배너 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/power/banner")
public class BannerController {

    private final AdminService adminService;
    private final BannerService bannerService;

    // 배너 목록 보기
    @Operation(summary = "특정 타입의 배너 목록 조회", description = "특정 타입의 배너를 전체 조회하는 API입니다.")
    @GetMapping
    public ResponseEntity<?> getAllBanners(@CurrentUser UserPrincipal userPrincipal,
                                           @Parameter(description = "배너 타입: HOME, PROJECT, IDEA", schema = @Schema(allowableValues = {"HOME", "PROJECT", "IDEA"}))
                                               @RequestParam String type,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        checkPermission(userPrincipal.getEmail());

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<BannerListRes> banners = bannerService.getAllBanners(Type.fromString(type), pageable);

        if (banners.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(banners)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // 배너 검색
    @Operation(summary = "특정 타입의 배너 검색", description = "특정 타입의 배너를 제목으로 검색하는 API입니다.")
    @GetMapping("/search")
    public ResponseEntity<?> searchHomeBanner(@CurrentUser UserPrincipal userPrincipal,
                                              @Parameter(description = "배너 타입: HOME, PROJECT, IDEA", schema = @Schema(allowableValues = {"HOME", "PROJECT", "IDEA"}))
                                          @RequestParam String type,
                                          @RequestParam String word,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {

        checkPermission(userPrincipal.getEmail());

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<BannerListRes> banners = bannerService.searchBanners(Type.fromString(type), word, pageable);

        if (banners.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(banners)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // 배너 상세 조회
    @Operation(summary = "배너 상세 조회", description = "배너를 상세 조회하는 API입니다.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getDetailBanner(@CurrentUser UserPrincipal userPrincipal,
                                             @PathVariable Long id) {
        checkPermission(userPrincipal.getEmail());

        BannerDetailRes bannerDetailRes = bannerService.getDetailBanner(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(bannerDetailRes)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // 배너 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBanner(@CurrentUser UserPrincipal userPrincipal,
                                          @PathVariable Long id,
                                          @RequestPart("title") String title,
                                          @RequestPart("file") MultipartFile file) throws IOException {
        checkPermission(userPrincipal.getEmail());

        BannerDetailRes bannerDetailRes = bannerService.updateBanner(file, title, id);
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(bannerDetailRes)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // 배너 삭제
    @Operation(summary = "배너 삭제", description = "배너를 삭제하는 API입니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBanner(@CurrentUser UserPrincipal userPrincipal,
                                          @PathVariable Long id) {
        checkPermission(userPrincipal.getEmail());

        bannerService.deleteBanner(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information("배너가 삭제되었습니다.")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // 배너 등록
    @Operation(summary = "배너 등록", description = "배너를 등록하는 API입니다.")
    @PostMapping
    public ResponseEntity<?> uploadBanner(@CurrentUser UserPrincipal userPrincipal,
                                          @RequestPart("title") String title,
                                          @Parameter(description = "배너 타입: HOME, PROJECT, IDEA", schema = @Schema(allowableValues = {"HOME", "PROJECT", "IDEA"}))
                                              @RequestPart("type") String type,
                                              @RequestPart("file") MultipartFile file) throws IOException {
        checkPermission(userPrincipal.getEmail());

        BannerDetailRes bannerDetailRes = bannerService.uploadBanner(file, title, Type.fromString(type), userPrincipal.getId());
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(bannerDetailRes)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    private void checkPermission(String email) {
        if (!adminService.isAdmin(email)) {
            throw new AccessDeniedException("해당 페이지에 대한 권한이 없습니다.");
        }
    }

}
