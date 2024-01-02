package depth.main.ideac.domain.banner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Setter
@Data
@Schema(description = "페이징된 배너 리스트 응답")
public class PagedBannerRes {

    @Schema(description = "페이지 번호")
    private int page;

    @Schema(description = "페이지 크기")
    private int size;

    @Schema(description = "전체 페이지 수")
    private int totalPages;

    @Schema(description = "전체 요소 수")
    private long totalElements;

    @Schema(description = "배너 리스트")
    private List<BannerListRes> banners;
}
