package depth.main.ideac.domain.my_page.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "내 게시물 조회 Response")
public class PostRes {
    private Long id;
    private String title;
    private String type;
    private LocalDateTime createdAt;
}
