package depth.main.ideac.domain.idea_post.presentation;

import depth.main.ideac.domain.idea_post.application.IdeaPostService;
import depth.main.ideac.domain.idea_post.dto.request.RegisterIdeaReq;
import depth.main.ideac.domain.idea_post.dto.request.UpdateIdeaReq;
import depth.main.ideac.global.config.security.token.CurrentUser;
import depth.main.ideac.global.config.security.token.UserPrincipal;
import depth.main.ideac.global.payload.ErrorResponse;
import depth.main.ideac.global.payload.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/idea")
public class IdeaPostController {
    private final IdeaPostService ideaPostService;


    @Operation(summary = "아이디어 등록", description = "아이디어를 등록한다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "아이디어 등록 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "아이디어 등록 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping("")
    ResponseEntity<?> registerIdea(@Parameter(description = "Access Token을 입력해주세요.", required = true)
                                   @CurrentUser UserPrincipal userPrincipal,
                                   @Valid @RequestBody RegisterIdeaReq registerIdeaReq){

        Long id = ideaPostService.registerIdea(userPrincipal, registerIdeaReq);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "글 전체 조회", description = "아이디어의 글을 정렬하여 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이디어 전체 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "아이디어 전체 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping
    public ResponseEntity<?> getLatestAllIdea(@Parameter(description = "정렬: createdAt, hits", schema = @Schema(allowableValues = {"createdAt", "hits"}))
                                              @RequestParam(defaultValue = "createdAt") String sortBy,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "12") int size) {
        return ideaPostService.getAllIdea(page, size, sortBy);
    }

    @Operation(summary = "글 상세 조회", description = "아이디어의 글을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이디어 상세 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "아이디어 상세 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getDetailIdea(@PathVariable Long id) {
        ideaPostService.addHitToRedis(id);
        return ideaPostService.getDetailIdea(id);
    }

    @Operation(summary = "글 수정", description = "아이디어의 글을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이디어 수정 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "아이디어 수정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateIdea(@CurrentUser UserPrincipal userPrincipal,
                                        @PathVariable Long id,
                                        @Valid @RequestBody UpdateIdeaReq updateIdeaReq) {
        return ideaPostService.updateIdea(id, userPrincipal.getId(), updateIdeaReq);
    }

    @Operation(summary = "글 삭제", description = "아이디어의 글을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이디어 삭제 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "아이디어 삭제 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteIdea(@CurrentUser UserPrincipal userPrincipal,
                                        @PathVariable Long id) {
        return ideaPostService.deleteIdea(id, userPrincipal.getId());
    }

    @Operation(summary = "권한 확인", description = "아이디어 수정/삭제 권한을 확인하는 API입니다. true: 가능, false: 불가능")
    @GetMapping("/check/{id}")
    private boolean checkPermission(@CurrentUser UserPrincipal userPrincipal,
                                    @PathVariable Long id) {
        return ideaPostService.isAdminOrWriter(id, userPrincipal.getId()); // true: 권한있음
    }
}
