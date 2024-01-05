package depth.main.ideac.domain.banner.presentation;

import depth.main.ideac.domain.banner.Type;
import depth.main.ideac.domain.banner.application.BannerService;
import depth.main.ideac.domain.banner.dto.BannerRes;
import depth.main.ideac.global.payload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Banner API", description = "배너 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/banner")
public class BannerController {

    private final BannerService bannerService;

    @Operation(summary = "특정 타입의 배너 노출", description = "특정 타입의 배너를 노출하는 API입니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = BannerRes.class))
            }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "No Content")
    })
    @GetMapping
    public ResponseEntity<?> viewBannersByType(@Parameter(description = "배너 타입: HOME, PROJECT, IDEA", schema = @Schema(allowableValues = {"HOME", "PROJECT", "IDEA"}))
                                                   @RequestParam String type) {
        List<BannerRes> banners = bannerService.viewBanners(Type.fromString(type));

        if (banners.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(banners)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
