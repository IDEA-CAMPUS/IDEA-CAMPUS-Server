package depth.main.ideac.domain.club_post.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateClubPostReq {

    @NotBlank(message = "제목을 입력해야 합니다.")
    @Size(max = 100, message = "제목은 최대 100자까지 입력 가능합니다.")
    private String title;

    @NotBlank(message = "내용을 입력해야 합니다.")
    private String description;

    private String url1;

    private String url2;

}
