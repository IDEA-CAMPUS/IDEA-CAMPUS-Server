package depth.main.ideac.domain.project_post.repository;

import depth.main.ideac.domain.project_post.ProjectPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectPostRepository extends JpaRepository<ProjectPost, Long> {

    Page<ProjectPost> findAll(Pageable pageable);
    Page<ProjectPost> findByBooleanWebAndBooleanAppAndBooleanAi(boolean booleanWeb, boolean booleanApp, boolean booleanAi, Pageable pageable);
    List<ProjectPost> findTop3ByOrderByCreatedAtDesc();
}
