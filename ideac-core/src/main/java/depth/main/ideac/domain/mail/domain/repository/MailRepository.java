package depth.main.ideac.domain.mail.domain.repository;

import depth.main.ideac.domain.mail.domain.Verify;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailRepository extends JpaRepository<Verify, Long> {
    Boolean existsByEmail(String email);

    void deleteByEmail(String email);

    Verify findByCode(String code);

    void findByEmail(String email);
}
