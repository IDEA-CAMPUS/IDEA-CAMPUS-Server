package depth.main.ideac.domain.mail.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class FindPasswordReq {
    @Schema( type = "string", example = "idea00@naver.com", description="이메일")
    @Email(message = "메일을 정확하게 입력해주세요")
    @NotBlank
    String email;

    @Schema( type = "string", example = "01045456565", description="전화번호")
    @Pattern(regexp = "^01([0|1|6|7|8|9])?([0-9]{3,4})?([0-9]{4})$", message = "번호를 정확하게 입력해주세요")
    @NotBlank
    String phoneNumber;
}
