package depth.main.ideac.domain.club_post.repository;

import depth.main.ideac.domain.club_post.ClubPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubPostRepository extends JpaRepository<ClubPost, Long> {
    Page<ClubPost> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
