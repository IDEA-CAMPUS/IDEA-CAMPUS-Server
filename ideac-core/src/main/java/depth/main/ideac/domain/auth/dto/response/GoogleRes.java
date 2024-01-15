package depth.main.ideac.domain.auth.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GoogleRes {

    private String email;
    private String name;
}
