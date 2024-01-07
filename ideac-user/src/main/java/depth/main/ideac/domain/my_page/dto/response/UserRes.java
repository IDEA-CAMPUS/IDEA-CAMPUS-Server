package depth.main.ideac.domain.my_page.dto.response;

import lombok.*;


public class UserRes {

    @Builder @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class HeaderInfoRes {
        private String nickname;
        private String color;
    }
}
