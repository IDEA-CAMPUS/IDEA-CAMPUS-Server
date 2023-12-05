package depth.main.ideac.domain.club_post.domain.repository;

import depth.main.ideac.domain.club_post.domain.ClubPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubPostRepository extends JpaRepository<ClubPost, Long> {
}
