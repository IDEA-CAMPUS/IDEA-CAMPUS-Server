package depth.main.ideac.domain.idea_post.repository;


import depth.main.ideac.domain.idea_post.IdeaPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdeaPostRepository extends JpaRepository<IdeaPost, Long> {
    List<IdeaPost> findTop3ByOrderByCreatedAtDesc();

    // @Query("SELECT i.hits FROM IdeaPost i WHERE i.id = :id")
    // Long findHitsById(Long id);

    @Modifying
    @Query("UPDATE IdeaPost i SET i.hits = i.hits + :hits WHERE i.id = :id")
    void updateHits(@Param("id") Long id, @Param("hits") Long hits);
}
