package depth.main.ideac.domain.project_post.repository;

import depth.main.ideac.domain.project_post.ProjectPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectPostRepository extends JpaRepository<ProjectPost, Long> {
}
