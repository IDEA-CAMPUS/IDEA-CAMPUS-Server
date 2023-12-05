package depth.main.ideac.domain.project_post.domain.repository;

import depth.main.ideac.domain.project_post.domain.ProjectPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectPostRepository extends JpaRepository<ProjectPost, Long> {
}
