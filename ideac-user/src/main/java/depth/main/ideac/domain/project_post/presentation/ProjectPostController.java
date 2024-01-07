package depth.main.ideac.domain.project_post.presentation;

import depth.main.ideac.domain.project_post.application.ProjectPostService;
import depth.main.ideac.domain.project_post.dto.request.PostProjectReq;
import depth.main.ideac.domain.project_post.dto.request.ProjectKeywordReq;
import depth.main.ideac.domain.project_post.dto.response.ProjectRes;
import depth.main.ideac.global.config.security.token.CurrentUser;
import depth.main.ideac.global.config.security.token.UserPrincipal;
import depth.main.ideac.global.payload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectPostController {

    private final ProjectPostService projectPostService;

    @Operation(summary = "프로젝트 게시", description = "프로젝트 게시글을 생성하는 API입니다.")
    @PostMapping("")
    public ResponseEntity<?> postProject(@CurrentUser UserPrincipal userPrincipal,
                                         @Valid @RequestBody PostProjectReq postProjectReq) {
        Long createdProjectId = projectPostService.postProject(userPrincipal.getId(), postProjectReq);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{project-id}")
                .buildAndExpand(createdProjectId)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "프로젝트 전체 조회", description = "프로젝트 게시글을 {size}개 최신 순 조회하는 API입니다.")
    @GetMapping("")
    public ResponseEntity<?> getAllProjects(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "12") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ProjectRes> projectRes = projectPostService.getAllProjects(pageable);
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
    public ResponseEntity<?> getProjectsByKeyword(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "12") int size,
                                                  @RequestBody ProjectKeywordReq projectKeywordReq) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ProjectRes> projectRes = projectPostService.getProjectsByKeyword(pageable, projectKeywordReq);
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
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(projectPostService.getProjectDetail(projectId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "프로젝트 수정", description = "프로젝트 게시글 한 건을 삭제하는 API입니다.")
    @PutMapping("/{project-id}")
    public ResponseEntity<?> updateProject(@CurrentUser UserPrincipal userPrincipal,
                                           @PathVariable("project-id") Long projectId,
                                           @Valid @RequestBody PostProjectReq updateProjectReq) {
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
}
