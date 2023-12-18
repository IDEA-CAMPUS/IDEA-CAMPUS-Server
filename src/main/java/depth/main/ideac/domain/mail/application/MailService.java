package depth.main.ideac.domain.mail.application;

import depth.main.ideac.domain.mail.domain.Verify;
import depth.main.ideac.domain.mail.domain.repository.MailRepository;
import depth.main.ideac.domain.mail.dto.FindPasswordReq;
import depth.main.ideac.domain.mail.dto.PassWordReq;
import depth.main.ideac.domain.user.domain.User;
import depth.main.ideac.domain.user.domain.repository.UserRepository;
import depth.main.ideac.global.DefaultAssert;
import depth.main.ideac.global.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final MailRepository mailRepository;
    private final PasswordEncoder passwordEncoder;
    @Transactional
    public ResponseEntity<?> sendEmail(FindPasswordReq findPasswordReq) {
        // 이메일이 존재하는지 확인
        Optional<User> user = userRepository.findByEmail(findPasswordReq.getEmail());
        DefaultAssert.isTrue(user.isPresent(), "존재하지 않은 유저입니다.");

        String code = RandomStringUtils.random(20, 33, 125, true, true);
        Verify verify = saveCode(findPasswordReq.getEmail(),code);


        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        try {
            // 1. 메일 수신자 설정
            String receiveList = findPasswordReq.getEmail();
            simpleMailMessage.setTo(receiveList);

            // 2. 메일 제목 설정
            simpleMailMessage.setSubject("test_title");

            // 3. 메일 내용 설정
            simpleMailMessage.setText("http://localhost:8080/mail/send-email/" + code);

            // 4. 메일 전송
            javaMailSender.send(simpleMailMessage);

        } catch (Exception e) {
            System.out.println("e = " + e);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(verify.getCode())
                .message("메일을 보냈어요!")
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 코드 저장
    private Verify saveCode(String email,String code) {
        if (mailRepository.existsByEmail(email)){
            mailRepository.deleteByEmail(email);
        }
        return mailRepository.save(new Verify(code,email));
    }

    @Transactional
    public ResponseEntity<?> changePassword(@Valid PassWordReq passWordReq, String code){
        Verify verify = mailRepository.findByCode(code);
        Optional<User> findUser = userRepository.findByEmail(verify.getEmail());

        User user = findUser.get();
        user.updatePassWord(passwordEncoder.encode(passWordReq.getPassword()));

//        userRepository.save(user);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(null)
                .message("비밀번호를 바꾸었어요")
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
