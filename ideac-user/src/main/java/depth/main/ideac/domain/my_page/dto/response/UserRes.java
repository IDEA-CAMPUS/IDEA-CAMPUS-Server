package depth.main.ideac.domain.my_page.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


public class UserRes {

    @Builder @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Schema(description = "Header에 표기될 회원 정보 Response")
    public static class HeaderInfoRes {
        private String nickname;
        private String color;
    }

    @Builder @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Schema(description = "마이페이지에 표기될 회원 정보 Request, 필요에 따라 필터링하여 사용")
    public static class MyPageInfoRes {
        private String color;
        private String name;
        private String nickname;
        private String email;
        private String phoneNumber;
        private String organization;
    }
}
