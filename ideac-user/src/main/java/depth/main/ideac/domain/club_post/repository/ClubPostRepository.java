package depth.main.ideac.domain.club_post.repository;

import depth.main.ideac.domain.club_post.ClubPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubPostRepository extends JpaRepository<ClubPost, Long> {
    Page<ClubPost> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<ClubPost> findTop3ByOrderByCreatedAtDesc();
}
