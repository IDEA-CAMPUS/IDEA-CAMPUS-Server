package depth.main.ideac.domain.project_post.repository;

import depth.main.ideac.domain.project_post.ProjectPostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectPostImageRepository extends JpaRepository<ProjectPostImage, Long> {
    List<ProjectPostImage> findByProjectPostId(Long projectId);
}
