package depth.main.ideac.domain.my_page.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostRes {
    private String title;
    private String type;
    private LocalDateTime createdAt;
}
