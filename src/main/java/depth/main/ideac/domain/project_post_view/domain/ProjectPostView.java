package depth.main.ideac.domain.project_post_view.domain;

import depth.main.ideac.domain.project_post.domain.ProjectPost;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPostView {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private ProjectPost projectPost;

    private int count;
}
