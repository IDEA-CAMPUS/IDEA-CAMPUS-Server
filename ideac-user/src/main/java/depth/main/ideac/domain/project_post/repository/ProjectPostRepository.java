package depth.main.ideac.domain.project_post.repository;

import depth.main.ideac.domain.project_post.ProjectPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectPostRepository extends JpaRepository<ProjectPost, Long> {
    Page<ProjectPost> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<ProjectPost> findTop3ByOrderByCreatedAtDesc();

    // @Query("SELECT p.hits FROM ProjectPost p WHERE p.id = :id")
    // Long findHitsById(Long id);

    @Modifying
    @Query("UPDATE ProjectPost p SET p.hits = p.hits + :hits WHERE p.id = :id")
    void updateHits(@Param("id") Long id, @Param("hits") Long hits);
}
