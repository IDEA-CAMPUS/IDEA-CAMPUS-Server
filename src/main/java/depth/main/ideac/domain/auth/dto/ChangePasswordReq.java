package depth.main.ideac.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePasswordReq {

    @Schema( type = "string", example = "string", description="기존 비밀번호 입니다.")
    @NotBlank
    @NotNull
    private String oldPassword;

    @Schema( type = "string", example = "string123", description="신규 비밀번호 입니다.")
    @NotBlank
    @NotNull
    private String newPassword;

    @Schema( type = "string", example = "string123", description="신규 비밀번호 확인란 입니다.")
    @NotBlank
    @NotNull
    private String reNewPassword;

}
