package depth.main.ideac.domain.idea_post.repository;


import depth.main.ideac.domain.idea_post.IdeaPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdeaPostRepository extends JpaRepository<IdeaPost, Long> {
    List<IdeaPost> findTop3ByOrderByCreatedAtDesc();
}
