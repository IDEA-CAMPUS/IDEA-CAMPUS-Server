package depth.main.ideac.domain.mail.presentation;

import depth.main.ideac.domain.mail.application.MailService;
import depth.main.ideac.domain.mail.dto.FindPasswordReq;
import depth.main.ideac.domain.mail.dto.PassWordReq;
import depth.main.ideac.global.payload.ErrorResponse;
import depth.main.ideac.global.payload.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Mail API", description = "Mail로 비밀번호 재설정하는 API입니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/mail")
public class MailController {
    final private MailService mailService;

    @Operation(summary = "메일보내기", description = "비밀번호를 찾기위해 메일을 보낸다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메일보내기 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "메일보내기 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping(value = "/send-email")
    public ResponseEntity<?> sendEmail(@Parameter(description = "Schemas의 FindPasswordReq를 참고해주세요.")
                                    @Valid @RequestBody FindPasswordReq findPasswordReq) {

        return mailService.sendEmail(findPasswordReq);
    }

    @PostMapping(value = "/change-passWord/{code}")
    public ResponseEntity<?> changePassWord(@Parameter(description = "Schemas의 PassWordReq를 참고해주세요.")
                                       @Valid @RequestBody PassWordReq passWordReq,
                                            @PathVariable String code) {

        return mailService.changePassword(passWordReq,code);
    }
}
