package depth.main.ideac.domain.project_post.presentation;

import depth.main.ideac.domain.project_post.application.ProjectPostService;
import depth.main.ideac.domain.project_post.dto.request.PostProjectReq;
import depth.main.ideac.global.config.security.token.CurrentUser;
import depth.main.ideac.global.config.security.token.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/project-post")
@RequiredArgsConstructor
public class ProjectPostController {

    private final ProjectPostService projectPostService;

    @Operation(summary = "프로젝트 게시", description = "프로젝트 게시글을 생성하는 API입니다.")
    @PostMapping("")
    public ResponseEntity<?> postProject(@CurrentUser UserPrincipal userPrincipal,
                                         @Valid @RequestBody PostProjectReq postProjectReq) {
        projectPostService.postProject(userPrincipal.getId(), postProjectReq);
        return ResponseEntity.ok().build();
    }
}
