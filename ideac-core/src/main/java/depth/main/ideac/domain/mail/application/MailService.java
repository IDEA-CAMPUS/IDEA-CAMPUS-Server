package depth.main.ideac.domain.mail.application;

import depth.main.ideac.domain.mail.domain.Verify;
import depth.main.ideac.domain.mail.domain.repository.MailRepository;
import depth.main.ideac.domain.mail.dto.FindPasswordReq;
import depth.main.ideac.domain.user.domain.User;
import depth.main.ideac.domain.user.domain.repository.UserRepository;
import depth.main.ideac.global.DefaultAssert;
import depth.main.ideac.global.payload.ApiResponse;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final MailRepository mailRepository;
    @Transactional
    public ResponseEntity<?> sendEmail(FindPasswordReq findPasswordReq) {
        // 이메일이 존재하는지 확인
        Optional<User> user = userRepository.findByEmail(findPasswordReq.getEmail());
        DefaultAssert.isTrue(user.isPresent(), "존재하지 않은 유저입니다.");
        String code = RandomStringUtils.random(20, 33, 125, true, true);
        saveCode(findPasswordReq.getEmail(),code);


        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            // 1. 메일 수신자 설정
            String receiveList = findPasswordReq.getEmail();
            message.addRecipients(MimeMessage.RecipientType.TO, receiveList);

            // 2. 메일 제목 설정
            message.setSubject("IDEA CAMPUS 비밀번호 재설정");

            // 3. 보내는 사람
            message.setFrom(new InternetAddress("ideac2587@gmail.com", "아이디어캠퍼스"));

            // 4. 메일 내용 설정
            String body = "<div>"
                    + "<h1>안녕하세요. IDEA CAMPUS입니다.</h1>"
                    + "<br>"
                    + "<p>서비스 이용을 위한 하단 계정의 비밀번호 재설정 이메일 요청 메일입니다.</p>"
                    + "<p>'비밀번호 재설정' 버튼을 클릭하여 재설정을 완료하실 수 있습니다.</p>"
                    + "<br>"
                    + "<a href='http://localhost:3000/resetPW" + "' style='"
                    + "display: inline-block;"
                    + "font-weight: bold;"
                    + "padding: 10px 20px;"
                    + "font-size: 16px;"
                    + "text-align: center;"
                    + "text-decoration: none;"
                    + "background-color: #B034F7;"  // 요청한 보라색 배경
                    + "color: white;"
                    + "border-radius: 10px;"  // 라운딩 처리
                    + "'>비밀번호 재설정</a>"
                    + "<p style='color: #A6A6A6; margin-top: 10px;'>비밀번호 재설정 메일은 1시간 후 만료됩니다.</p> "
                    + "<p style='color: #A6A6A6; margin-top: 10px;'>만료 시, 비밀번호 찾기를 통해 메일을 재요청해 주세요.</p>"
                    + "</div>";





            message.setText(body, "utf-8", "html");// 내용, charset 타입, subtype
            // 4. 메일 전송
            javaMailSender.send(message);

        } catch (Exception e) {
            System.out.println("e = " + e);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information("http://localhost:3000/resetPW" + code)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 코드 저장
    private Verify saveCode(String email,String code) {

        // 메일을 보내기만하고 인증을 안했을경우 이미있던 메일 삭제하고 진행
        if (mailRepository.existsByEmail(email)){
            mailRepository.deleteByEmail(email);
        }
        return mailRepository.save(new Verify(code,email, LocalDateTime.now().plusHours(1)));
    }


}
