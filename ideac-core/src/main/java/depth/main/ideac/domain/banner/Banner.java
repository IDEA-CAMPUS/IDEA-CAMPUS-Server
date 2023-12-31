package depth.main.ideac.domain.banner;

import depth.main.ideac.domain.common.BaseEntity;
import depth.main.ideac.domain.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Banner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "제목이 입력되지 않았습니다.")
    private String title;

    @NotBlank(message = "파일이 업로드되지 않았습니다.")
    private String fileName;

    private String saveFileUrl;

    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void updateBanner(String title, String fileName, String saveFileUrl) {
        this.title = title;
        this.fileName = fileName;
        this.saveFileUrl = saveFileUrl;
    }
}
