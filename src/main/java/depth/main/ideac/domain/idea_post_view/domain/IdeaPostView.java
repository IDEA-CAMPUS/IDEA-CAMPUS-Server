package depth.main.ideac.domain.idea_post_view.domain;

import depth.main.ideac.domain.idea_post.domain.IdeaPost;
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
public class IdeaPostView {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private IdeaPost ideaPost;

    private int count;
}
