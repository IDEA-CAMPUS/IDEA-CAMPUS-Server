package depth.main.ideac.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;

@Getter
public class SignUpReq {

    @Schema( type = "string", example = "idea00@naver.com", description="아이디(이메일)")
    @Email
    private String idEmail;

    @Schema( type = "string", example = "홍길동", description="이름")
    @NotBlank
    private String name;

    @Schema( type = "string", example = "스프링화이팅", description="닉네임")
    @NotBlank
    private String nickname;

    @Schema( type = "string", example = "abc12345678", description="비밀번호")
    @NotBlank
    private String password;

    @Schema( type = "string", example = "abc12345678", description="비밀번호 한번 더 확인")
    @NotBlank
    private String checkPassword;

    @Schema( type = "string", example = "01012341234", description="휴대폰번호")
    @NotBlank
    private String phoneNumber;

    @Schema( type = "string", example = "depth", description="소속 동아리")
    @NotBlank
    private String organization;

    @Schema( type = "string", example = "True", description="마케팅 수신 동의 여부확인")
    private boolean agreeMarketingSms;
}
