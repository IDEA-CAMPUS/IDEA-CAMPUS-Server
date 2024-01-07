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

    @Builder @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class MyPageInfoRes {
        private String color;
        private String name;
        private String nickname;
        private String email;
        private String phoneNumber;
        private String organization;
    }
}
