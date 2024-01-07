package depth.main.ideac.domain.my_page.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "회원정보 수정 Request")
public class UpdateUserReq {

    @Size(min = 2, message = "2자 이상 입력해주세요")
    private String name;

    @Column(unique = true)
    private String nickname;

    @Email(message = "이메일 형식이어야 합니다.")
    @NotBlank(message = "이메일이 입력되지 않았습니다.")
    private String email;

    @Pattern(regexp = "^01([0|1|6|7|8|9])?([0-9]{3,4})?([0-9]{4})$", message = "번호를 정확하게 입력해주세요")
    private String phoneNumber;

    private String organization;

    private String color;

}
