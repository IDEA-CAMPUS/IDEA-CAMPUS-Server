package depth.main.ideac.domain.mail.domain;

import depth.main.ideac.domain.common.BaseEntity;
import depth.main.ideac.domain.mail.dto.FindPasswordReq;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Verify extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String code;
    @Email
    String email;

    LocalDateTime expirationDate;

    public Verify(String code, String email, LocalDateTime expirationDate) {
        this.code = code;
        this.email = email;
        this.expirationDate = expirationDate;
    }
    public boolean checkExpiration(LocalDateTime nowTime){
        System.out.println("nowTime = " + nowTime);
        System.out.println("expirationDate = " + expirationDate);
        return nowTime.isBefore(expirationDate);
    }
}
