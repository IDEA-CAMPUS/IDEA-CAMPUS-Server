package depth.main.ideac.domain.admin.dto;

import depth.main.ideac.domain.user.domain.Role;
import depth.main.ideac.domain.user.domain.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserRes {

    @Schema( type = "Long", example = "1" , description="사용자 id")
    private Long id;

    @Schema( type = "String", example = "닉네임01" , description="사용자 닉네임")
    private String nickName;

    @Schema( type = "String", example = "이름" , description="사용자 이름")
    private String name;

    @Schema( type = "String", example = "abc001@gmail.com" , description="사용자 이메일")
    private String email;

    @Schema( type = "String", example = "01000000000" , description="사용자 전화번호")
    private String phoneNumber;

    @Schema( type = "LocalDateTime", example = "2023-12-22 18:32:03.977986" , description="사용자 가입 일시")
    private LocalDateTime createdAt;

    @Schema( type = "String", example = "ACTIVE" , description="사용자 상태")
    private Status status;

    @Schema( type = "String", example = "USER" , description="사용자 권한")
    private Role role;


}
