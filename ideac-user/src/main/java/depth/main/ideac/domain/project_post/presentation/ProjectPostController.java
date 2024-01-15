package depth.main.ideac.domain.project_post.presentation;

import depth.main.ideac.domain.project_post.application.ProjectPostService;
import depth.main.ideac.domain.project_post.dto.request.PostProjectReq;
import depth.main.ideac.domain.project_post.dto.request.ProjectKeywordReq;
import depth.main.ideac.domain.project_post.dto.response.ProjectRes;
import depth.main.ideac.global.config.security.token.CurrentUser;
import depth.main.ideac.global.config.security.token.UserPrincipal;
import depth.main.ideac.global.payload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
@Tag(name = "ProjectPost API", description = "프로젝트 갤러리 관련 API입니다.")
public class ProjectPostController {

    private final ProjectPostService projectPostService;

    @Operation(summary = "프로젝트 게시", description = "프로젝트 게시글을 생성하는 API입니다.")
    @PostMapping(path= "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> postProject(@CurrentUser UserPrincipal userPrincipal,
                                         @Valid @ModelAttribute PostProjectReq postProjectReq) throws IOException {
        Long createdProjectId = projectPostService.postProject(userPrincipal.getId(), postProjectReq);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{project-id}")
                .buildAndExpand(createdProjectId)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "프로젝트 전체 조회", description = "프로젝트 게시글을 정렬하여 조회하는 API입니다.")
    @GetMapping
    public ResponseEntity<?> getAllProjects(
            @Parameter(description = "정렬: createdAt, hits", schema = @Schema(allowableValues = {"createdAt", "hits"}))
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Page<ProjectRes> projectRes = projectPostService.getAllProjects(page, size, sortBy);
        if (projectRes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(projectRes)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "프로젝트 키워드별 조회", description = "프로젝트 게시글을 키워드에 따라 {size}개 최신 순 조회하는 API입니다.")
    @GetMapping("/keyword")
    public ResponseEntity<?> getProjectsByKeyword(@Parameter(description = "정렬: createdAt, hits", schema = @Schema(allowableValues = {"createdAt", "hits"}))
                                                  @RequestParam(defaultValue = "createdAt") String sortBy,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "12") int size,
                                                  @RequestParam(defaultValue = "false") boolean booleanWeb,
                                                  @RequestParam(defaultValue = "false") boolean booleanApp,
                                                  @RequestParam(defaultValue = "false") boolean booleanAi) {
        Page<ProjectRes> projectRes = projectPostService.getProjectsByKeyword(page, size, sortBy, booleanWeb, booleanApp, booleanAi);
        if (projectRes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(projectRes)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "프로젝트 상세 조회", description = "프로젝트 게시글 한 건을 상세 조회하는 API입니다.")
    @GetMapping("/{project-id}")
    public ResponseEntity<?> getProjectDetail(@PathVariable("project-id") Long projectId) {
        projectPostService.addHitToRedis(projectId);
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(projectPostService.getProjectDetail(projectId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "프로젝트 수정", description = "프로젝트 게시글 한 건을 수정하는 API입니다.")
    @PutMapping(path = "/{project-id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProject(@CurrentUser UserPrincipal userPrincipal,
                                           @PathVariable("project-id") Long projectId,
                                           @Valid @ModelAttribute PostProjectReq updateProjectReq) throws IOException {
        projectPostService.updateProject(userPrincipal.getId(), projectId, updateProjectReq);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "프로젝트 삭제", description = "프로젝트 게시글 한 건을 삭제하는 API입니다.")
    @DeleteMapping("/{project-id}")
    public ResponseEntity<?> deleteProject(@CurrentUser UserPrincipal userPrincipal,
                                           @PathVariable("project-id") Long projectId) {
        projectPostService.deleteProject(userPrincipal.getId(), projectId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "권한 확인", description = "프로젝트 수정/삭제 권한을 확인하는 API입니다. true: 가능, false: 불가능")
    @GetMapping("/check/{id}")
    private boolean checkPermission(@CurrentUser UserPrincipal userPrincipal,
                                    @PathVariable Long id) {
        return projectPostService.isAdminOrWriter(id, userPrincipal.getId()); // true: 권한있음
    }
}
