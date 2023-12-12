package depth.main.ideac.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class SignInReq {

    @Schema( type = "string", example = "idea00@naver.com", description="계정 이메일 입니다.")
    @NotBlank
    @Email
    private String email;

    @Schema( type = "string", example = "abc12345678", description="계정 비밀번호 입니다.")
    @NotBlank
    private String password;

}
