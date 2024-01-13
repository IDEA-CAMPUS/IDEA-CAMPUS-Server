package depth.main.ideac.domain.home.presentation;

import depth.main.ideac.domain.club_post.dto.response.ClubPostRes;
import depth.main.ideac.domain.home.application.HomeService;
import depth.main.ideac.domain.idea_post.dto.response.GetAllIdeasRes;
import depth.main.ideac.domain.project_post.dto.response.ProjectRes;
import depth.main.ideac.global.payload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "HOME API", description = "홈 화면 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/home")
public class HomeController {

    private final HomeService homeService;

    @Operation(summary = "동아리/학회 글 미리보기", description = "홈화면에서 동아리/학회 글을 보여주는 API입니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ClubPostRes.class))})
    @GetMapping("/club")
    public ResponseEntity<?> getHomeClubs() {

        List<ClubPostRes> posts = homeService.getClubs();
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(posts)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "아이디어 존 글 미리보기", description = "홈화면에서 아이디어 존 글을 보여주는 API입니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GetAllIdeasRes.class))})
    @GetMapping("/idea")
    public ResponseEntity<?> getHomeIdeas() {

        List<GetAllIdeasRes> posts = homeService.getIdeas();
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(posts)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "프로젝트 갤러리 글 미리보기", description = "홈화면에서 프로젝트 갤러리 글을 보여주는 API입니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProjectRes.class))})
    @GetMapping("/project")
    public ResponseEntity<?> getHomeProjects() {

        List<ProjectRes> posts = homeService.getProjects();
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(posts)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
