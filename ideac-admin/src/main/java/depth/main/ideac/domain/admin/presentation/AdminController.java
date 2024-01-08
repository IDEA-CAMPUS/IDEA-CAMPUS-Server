package depth.main.ideac.domain.admin.presentation;

import depth.main.ideac.domain.admin.application.AdminService;
import depth.main.ideac.domain.admin.dto.UserRes;
import depth.main.ideac.global.config.security.token.CurrentUser;
import depth.main.ideac.global.config.security.token.UserPrincipal;
import depth.main.ideac.global.payload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin API", description = "관리자 기능 관련 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/power")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "사용자 전체 조회", description = "사용자를 전체 조회하는 API입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserRes.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "No Content")
    })
    @GetMapping
    public ResponseEntity<?> getAllUsers(@CurrentUser UserPrincipal userPrincipal,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {

        checkPermission(userPrincipal.getEmail());

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<UserRes> users = adminService.getAllUsers(pageable);

        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(users)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    // 검색
    @Operation(summary = "사용자 검색", description = "사용자를 닉네임 또는 이름으로 검색하는 API입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserRes.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "No Content")
    })
    @GetMapping("/search")
    public ResponseEntity<?> searchUser(@CurrentUser UserPrincipal userPrincipal,
                                        @RequestParam String word,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {

        checkPermission(userPrincipal.getEmail());

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<UserRes> users = adminService.searchUser(word, pageable);

        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(users)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "사용자 상태 변경", description = "사용자의 상태를 변경하는 API입니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserRes.class))})
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@CurrentUser UserPrincipal userPrincipal,
                                          @PathVariable Long id) {
        checkPermission(userPrincipal.getEmail());

        UserRes userRes = adminService.setStatus(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(userRes)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "사용자 역할 변경", description = "사용자의 역할을 변경하는 API입니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserRes.class))})
    // @PreAuthorize("hasRole('OWNER')")
    @PatchMapping("/{id}/role")
    public ResponseEntity<?> updateRole(@CurrentUser UserPrincipal userPrincipal,
                                        @PathVariable Long id) {
        // OWNER가 아닐 경우 예외 처리
        checkOwner(userPrincipal.getId());

        UserRes userRes = adminService.setRole(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(userRes)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    private void checkPermission(String email) {
        if (!adminService.isAdmin(email)) {
            throw new AccessDeniedException("해당 페이지에 대한 권한이 없습니다.");
        }
    }

    private void checkOwner(Long id) {
        if (!adminService.isOwner(id)) {
            throw new AccessDeniedException("해당 기능에 대한 권한이 없습니다.");
        }
    }
}
