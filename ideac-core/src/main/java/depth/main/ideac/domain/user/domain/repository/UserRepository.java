package depth.main.ideac.domain.user.domain.repository;

import depth.main.ideac.domain.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByPhoneNumber(String phoneNumber);


    Boolean existsByNickname(String nickname);

    @Query("SELECT u FROM User u WHERE u.status != 'DELETE'")
    Page<User> getUsersByStatusNotDelete(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.status != 'DELETE' AND (LOWER(u.name) LIKE %:word% OR LOWER(u.nickname) LIKE %:word%)")
    Page<User> getUsersByStatusNotDeleteAndNameOrNicknameContainingIgnoreCase(@Param("word") String word, Pageable pageable);
}
