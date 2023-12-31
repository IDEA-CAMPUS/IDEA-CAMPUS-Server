package depth.main.ideac.domain.banner.dto;

import depth.main.ideac.domain.banner.Type;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BannerDetailRes {

    private Long id;

    private String title;

    private String originalFileName;

    private String saveFileName;

    private String contentType;

    private Type type;

    private LocalDateTime createdAt;

    private String nickName;
}
