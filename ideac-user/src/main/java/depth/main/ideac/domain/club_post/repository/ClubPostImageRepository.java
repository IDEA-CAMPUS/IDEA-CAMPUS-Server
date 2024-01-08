package depth.main.ideac.domain.club_post.repository;

import depth.main.ideac.domain.club_post.ClubPostImage;
import depth.main.ideac.domain.project_post.ProjectPostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubPostImageRepository extends JpaRepository<ClubPostImage, Long> {

    List<ClubPostImage> findByClubPostId(Long clubId);
}
