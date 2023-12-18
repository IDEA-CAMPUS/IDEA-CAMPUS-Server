package depth.main.ideac.domain.mail.domain;

import depth.main.ideac.domain.mail.dto.FindPasswordReq;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Verify {
    @Id
    String code;

    @Email
    String email;
}
