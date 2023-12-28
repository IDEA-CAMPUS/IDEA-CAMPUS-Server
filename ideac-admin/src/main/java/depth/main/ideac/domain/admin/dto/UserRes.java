package depth.main.ideac.domain.admin.dto;

import depth.main.ideac.domain.user.domain.Role;
import depth.main.ideac.domain.user.domain.Status;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserRes {

    private Long id;
    private String nickName;
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private Status status;
    private Role role;


}
