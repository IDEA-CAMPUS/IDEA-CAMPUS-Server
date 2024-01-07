package depth.main.ideac.domain.club_post.repository;

import depth.main.ideac.domain.club_post.ClubPostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubPostImageRepository extends JpaRepository<ClubPostImage, Long> {

}
