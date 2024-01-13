package depth.main.ideac.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RefreshTokenReq {

    @Schema( type = "string", example = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2NTI3OTgxOTh9.6CoxHB_siOuz6PxsxHYQCgUT1_QbdyKTUwStQDutEd1-cIIARbQ0cyrnAmpIgi3IBoLRaqK7N1vXO42nYy4g5g", description="refresh token 입니다." )
    @NotBlank
    @NotNull
    private String refreshToken;

    public RefreshTokenReq(){}

    @Builder
    public RefreshTokenReq(String refreshToken){
        this.refreshToken = refreshToken;
    }
}
