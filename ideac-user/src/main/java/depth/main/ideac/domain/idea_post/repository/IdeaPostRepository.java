package depth.main.ideac.domain.idea_post.repository;


import depth.main.ideac.domain.idea_post.IdeaPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdeaPostRepository extends JpaRepository<IdeaPost, Long> {
}