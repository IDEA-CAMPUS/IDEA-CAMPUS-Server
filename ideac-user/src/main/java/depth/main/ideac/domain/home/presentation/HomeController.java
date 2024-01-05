package depth.main.ideac.domain.home.presentation;

import depth.main.ideac.domain.club_post.dto.ClubPostRes;
import depth.main.ideac.domain.home.application.HomeService;
import depth.main.ideac.domain.idea_post.dto.GetAllIdeasRes;
import depth.main.ideac.global.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/home")
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/club")
    public ResponseEntity<?> getHomeClubs() {

        List<ClubPostRes> posts = homeService.getClubs();
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(posts)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/idea")
    public ResponseEntity<?> getHomeIdeas() {

        List<GetAllIdeasRes> posts = homeService.getIdeas();
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(posts)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
