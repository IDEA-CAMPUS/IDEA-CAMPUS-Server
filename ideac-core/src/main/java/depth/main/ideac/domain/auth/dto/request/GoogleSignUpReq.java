package depth.main.ideac.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class GoogleSignUpReq {

    @Schema( type = "string", example = "스프링화이팅", description="닉네임")
    @NotBlank
    private String nickname;

    @Schema( type = "string", example = "01012341234", description="휴대폰번호")
    @NotBlank
    @Pattern(regexp = "^01([0|1|6|7|8|9])?([0-9]{3,4})?([0-9]{4})$", message = "번호를 정확하게 입력해주세요")
    private String phoneNumber;

    @Schema( type = "string", example = "depth", description="소속 동아리")
    @NotBlank
    private String organization;

    @Schema( type = "string", example = "True", description="마케팅 수신 동의 여부확인")
    private boolean agreeMarketingSms;
}
